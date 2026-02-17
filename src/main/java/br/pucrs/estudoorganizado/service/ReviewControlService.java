package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.RegisterStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.ReviewControlEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import br.pucrs.estudoorganizado.entity.map.StudyRecordMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.ReviewControlRepository;
import br.pucrs.estudoorganizado.repository.StudyRecordRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewControlService {

    private final TopicRepository topicRepository;
    private final ReviewControlRepository repository;
    private final StudyRecordRepository studyRecordRepository;

    public void startedReview(Long topicId) {
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.TOPIC_NOT_FOUND));

        if (topic.getReviewIntervalsDays().isEmpty()) return;

        ReviewControlEntity review = new ReviewControlEntity();
        review.setTopic(topic);
        review.setSequenceIndex(0);
        review.setStatus(ReviewStatusEnum.PLANNED);

        scheduleNextReview(topic, review);
        repository.save(review);
    }


    /**
     * Registra a execução de uma revisão e avança o agendamento da próxima.
     * <p>
     * Fluxo:
     * 1. Busca o controle de revisão pendente do tópico
     * 2. Salva o StudyRecord da revisão realizada
     * 3. Calcula e agenda a próxima revisão
     *
     * @param topicId ID do tópico revisado
     * @param request Dados da revisão realizada
     */
    public void recordReview(Long topicId, RegisterStudyRecordDTO request) {

        ReviewControlEntity reviewControl = repository
                .findByTopicId(topicId)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.REVIEW_NOT_FOUND));

        studyRecordRepository.save(
                StudyRecordMapper.toReviewRecord(reviewControl, request)
        );

        nextReview(reviewControl.getTopic(), reviewControl);
    }

    /**
     * Calcula e agenda a próxima revisão com base na sequência de intervalos do tópico.
     * <p>
     * Regras:
     * - O sequenceIndex representa a posição atual na lista de intervalos
     * - Se ainda houver próximos intervalos, incrementa o índice
     * - Se não houver, marca como DONE
     * - A próxima data é hoje + intervalo de dias
     *
     * @param topic         Tópico da revisão
     * @param reviewControl Controle de revisão atual
     */
    private void nextReview(TopicEntity topic, ReviewControlEntity reviewControl) {

        List<Integer> intervals = topic.getReviewIntervalsDays();
        if (intervals == null || intervals.isEmpty()) {
            reviewControl.setStatus(ReviewStatusEnum.DONE);
            repository.save(reviewControl);
            return;
        }

        int currentIndex = reviewControl.getSequenceIndex() == null ? 0 : reviewControl.getSequenceIndex();
        int nextIndex = currentIndex + 1;

        if (nextIndex >= intervals.size()) {
            reviewControl.setStatus(ReviewStatusEnum.DONE);
            repository.save(reviewControl);
            return;
        }

        reviewControl.setSequenceIndex(nextIndex);
        reviewControl.setStatus(ReviewStatusEnum.PLANNED);
        scheduleNextReview(topic, reviewControl);
        repository.save(reviewControl);
    }

    /**
     * Define a data da próxima revisão com base no índice atual da sequência.
     */
    private void scheduleNextReview(TopicEntity topic, ReviewControlEntity reviewControl) {
        int index = reviewControl.getSequenceIndex();
        int daysToAdd = topic.getReviewIntervalsDays().get(index);

        reviewControl.setScheduleDate(LocalDate.now().plusDays(daysToAdd));
    }

    /**
     * Atualiza automaticamente o status das revisões com base na data agendada.
     * Regras:
     * - Se a data ainda não chegou → PLANNED
     * - Se é hoje → PENDING
     * - Se já passou → DELAYED
     *  PASSIVEL DE SCHEDULE, ENQUANTO NAO ATIVADO, SERA CHAMADO NO GET
     * Esse método deve ser executado periodicamente (ex: ao abrir dashboard ou via scheduler).
     */
    public void updateReviewStatuses() {

        LocalDate today = LocalDate.now();

        List<ReviewControlEntity> reviews =
                repository.findAllByStatusNot(ReviewStatusEnum.DONE);

        for (ReviewControlEntity review : reviews) {

            if (review.getScheduleDate() == null) continue;

            ReviewStatusEnum newStatus;

            if (review.getScheduleDate().isAfter(today)) {
                newStatus = ReviewStatusEnum.PLANNED;

            } else if (review.getScheduleDate().isEqual(today)) {
                newStatus = ReviewStatusEnum.PENDING;

            } else {
                newStatus = ReviewStatusEnum.DELAYED;
            }

            if (review.getStatus() != newStatus) {
                review.setStatus(newStatus);
            }
        }

        repository.saveAll(reviews);
    }

}
