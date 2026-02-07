package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.TopicStudyHistoryView;
import br.pucrs.estudoorganizado.service.utils.Utils;
import java.util.Optional;

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

    private static <T extends TopicSummaryDTO> T fillSummaryFields(T dto, TopicEntity entity) {
        dto.id = entity.getId();
        dto.order = entity.getOrder();
        dto.description = entity.getDescription();
        dto.color = Utils.resolveTopicColor(entity.getIncidenceScore(), entity.getKnowledgeScore());
        dto.elapsedTime = "0min";
        dto.score = "-%";
        return dto;
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
