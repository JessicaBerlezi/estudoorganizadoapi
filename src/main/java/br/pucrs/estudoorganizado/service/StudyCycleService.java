package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectTopicDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;

import br.pucrs.estudoorganizado.repository.StudyCycleItemRepository;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import br.pucrs.estudoorganizado.controller.MocksFactory;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCycleService {
    private static final Logger logger = LoggerFactory.getLogger(StudyCycleService.class);

    private final StudyCycleRepository studyCycleRepository;
    private final TopicRepository topicRepository;


    public void create(InsertStudyCycleDTO dto) {

        List<StudyCycleItemEntity> saveAt = new ArrayList<>();

        StudyCycleEntity cycle = StudyCycleEntity.fromDTO(dto);

        for (SubjectTopicDTO itemDTO : dto.getTopics()) {

            TopicEntity topic = topicRepository.findById(itemDTO.getIdTopic()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found: " + itemDTO.getIdTopic()));

            saveAt.add(new StudyCycleItemEntity(cycle, topic));
        }
        cycle.setItems(saveAt);
        studyCycleRepository.save(cycle);
    }

    public List<TopicSummaryDTO> getTopicsBySubjectId(Long id) {
        LinkedList<SubjectDTO> subjects = MocksFactory.createSubjectDTOMock();

        List<TopicSummaryDTO> topics = getTopicsBySubjectId(subjects, id);
        if (!topics.isEmpty()) {
            logger.info("Topics by subject find: {}", topics);
        } else {
            logger.info("Subject not find for id: {}", id);
        }
        return topics;
    }

    private static List<TopicSummaryDTO> getTopicsBySubjectId(LinkedList<SubjectDTO> subjects, Long id) {
        for (SubjectDTO subject : subjects) {
            if (Objects.equals(subject.getId(), id)) {
                return subject.getTopics();
            }
        }
        return List.of();
    }
}
