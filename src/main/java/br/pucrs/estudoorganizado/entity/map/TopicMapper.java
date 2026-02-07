package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyRecordEntity;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.TopicStudyHistoryView;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopicMapper {

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_, _, _ -> new")
    public static TopicEntity convertToEntity(InsertTopicDTO dto, SubjectEntity subject, Integer order) {
        return new TopicEntity(
                dto.description,
                order,
                dto.incidenceScore,
                dto.knowledgeScore,
                dto.reviewIntervals,
                dto.annotation,
                subject
        );
    }

    public static TopicSummaryDTO convertToSummaryDTO(TopicEntity entity) {
        return fillSummaryFields(new TopicSummaryDTO(), entity);
    }


    public static TopicDetailDTO convertToDetailDTO(TopicEntity entity) {
        TopicDetailDTO dto = fillSummaryFields(new TopicDetailDTO(), entity);
        dto.annotation = entity.getAnnotation();
        dto.subject = entity.getSubject().getDescription();
        return dto;
    }

    private static <T extends TopicSummaryDTO> T fillSummaryFields(T dto, TopicEntity entity) {
        dto.id = entity.getId();
        dto.order = entity.getOrder();
        dto.description = entity.getDescription();
        dto.color = Utils.resolveTopicColor(entity.getIncidenceScore(), entity.getKnowledgeScore());
        dto.elapsedTime = "0min";
        dto.score = "-%";
        return dto;
    }

    public static TopicReviewDetailDTO convertToHistoryDTO(TopicEntity entity, List<StudyRecordEntity> histories) {
        TopicReviewDetailDTO dto = fillSummaryFields(new TopicReviewDetailDTO(), entity);

        List<TopicHistoryDTO> historyDTOs = histories.stream()
                .sorted(Comparator.comparing(StudyRecordEntity::getStartedAt).reversed())
                .map(StudyRecordMapper::toHistoryDTO)
                .toList();

        dto.setHistory(historyDTOs);
        dto.setReviewInfo(buildReviewInfo(entity));
        return dto;
    }

    public static String buildReviewInfo(TopicEntity entity) {
        List<Integer> intervals = entity.getReviewIntervalsDays();
        if (intervals == null || intervals.isEmpty()) {
            return "Sem revisões agendadas";
        }
        String joined = intervals.stream()
                .sorted()
                .map(d -> d + "d")
                .collect(Collectors.joining(", "));
        return "Agenda de próximas revisões: " + joined;
    }

    public static TopicWithHistoryDTO toTopicDTO(TopicStudyHistoryView row) {
        TopicWithHistoryDTO dto = new TopicWithHistoryDTO();
        dto.setId(row.getTopicId());
        dto.setOrder(row.getTopicOrder());
        dto.setColor(Utils.resolveTopicColor(row.getTopicIncidenceScore(), row.getTopicKnowledgeScore()));
        dto.setDescription(row.getTopicDescription());
        dto.setSubject(row.getSubjectDescription());
        dto.setElapsedTime(Utils.formatDurationMinutes(row.getTopicTotalDurationMinutes()));
        dto.setScore(row.getTopicAvgScore() == null ?  "-": row.getTopicAvgScore() + "%");
        dto.setAnnotation(row.getTopicAnnotation());
        return dto;
    }

    public static TopicHistoryDTO toHistoryDTO(TopicStudyHistoryView row) {
        TopicHistoryDTO dto = new TopicHistoryDTO();

        String label = switch (row.getStudyType()) {
            case STUDY_CYCLE -> "Estudo";
            case REVIEW -> "Revisão";
        };

        dto.description = label + " " + row.getRecordStartedAt().format(Utils.DATE_FMT);

        int solved = Optional.ofNullable(row.getQuestionsSolved()).orElse(0);
        int incorrect = Optional.ofNullable(row.getQuestionsIncorrected()).orElse(0);
        int correct = Math.max(0, solved - incorrect);

        dto.information = solved + "Q " + correct + "A " +
                Math.round(Optional.ofNullable(row.getQuestionsPercent()).orElse(0.0)) + "%";

        dto.annotation = row.getRecordAnnotation();

        return dto;
    }
}
