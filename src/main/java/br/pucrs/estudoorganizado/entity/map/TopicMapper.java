package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import br.pucrs.estudoorganizado.entity.view.CycleStudyView;
import br.pucrs.estudoorganizado.entity.view.TopicWithHistoryView;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.LocalDate;
import java.util.*;

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

    public static TopicWithHistoryDTO toTopicDTO(CycleStudyView row) {
        TopicWithHistoryDTO dto = new TopicWithHistoryDTO();
        dto.setId(row.getTopicId());
        dto.setOrder(row.getTopicOrder());
        dto.setColor(Utils.resolveTopicColor(row.getTopicIncidenceScore(), row.getTopicKnowledgeScore()));
        dto.setDescription(row.getTopicDescription());
        dto.setSubject(row.getSubjectDescription());
        dto.setElapsedTime(Utils.formatDurationMinutes(row.getTopicTotalDurationMinutes()));
        dto.setScore(row.getTopicAvgScore() == null ? "-" : row.getTopicAvgScore() + "%");
        dto.setAnnotation(row.getTopicAnnotation());

        dto.getHistory().add(toHistoryDTO(
                row.getStudyType(),
                row.getRecordStartedAt(),
                row.getQuestionsSolved(),
                row.getQuestionsIncorrected(),
                row.getQuestionsPercent(),
                row.getRecordAnnotation()
        ));

        return dto;
    }

    private static TopicHistoryDTO toHistoryDTO(
            StudyTypeEnum studyType,
            LocalDate recordStartedAt,
            Integer questionsSolved,
            Integer questionsIncorrected,
            Double questionsPercent,
            String recordAnnotation) {
        TopicHistoryDTO dto = new TopicHistoryDTO();

        String label = switch (studyType) {
            case STUDY_CYCLE -> "Estudo";
            case REVIEW -> "Revisão";
        };

        dto.description = label + " " + recordStartedAt.format(Utils.DATE_FMT);

        int solved = Optional.ofNullable(questionsSolved).orElse(0);
        int incorrect = Optional.ofNullable(questionsIncorrected).orElse(0);
        int correct = Math.max(0, solved - incorrect);

        dto.information = solved + "Q " + correct + "A " +
                Math.round(Optional.ofNullable(questionsPercent).orElse(0.0)) + "%";

        dto.annotation = recordAnnotation;

        return dto;
    }

    public static List<TopicWithHistoryDTO> toTopicWithHistoryDTOList(List<TopicWithHistoryView> rows) {

        Map<Long, TopicWithHistoryDTO> topicsMap = new LinkedHashMap<>();

        for (TopicWithHistoryView row : rows) {

            TopicWithHistoryDTO topicDTO = topicsMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicWithHistoryDTO dto = new TopicWithHistoryDTO();
                        dto.setId(row.getTopicId());
                        dto.setOrder(row.getTopicOrder());
                        dto.setColor(Utils.resolveTopicColor(
                                row.getTopicIncidenceScore(),
                                row.getTopicKnowledgeScore()
                        ));
                        dto.setDescription(row.getTopicDescription());
                        dto.setSubject(row.getSubjectDescription());
                        dto.setElapsedTime(Utils.formatDurationMinutes(row.getTopicTotalDurationMinutes()));
                        dto.setScore(row.getTopicAvgScore() == null ? "-" : row.getTopicAvgScore() + "%");
                        dto.setAnnotation(row.getTopicAnnotation());
                        dto.setHistory(new ArrayList<>());
                        return dto;
                    }
            );

            topicDTO.getHistory().add(toHistoryDTO(
                    row.getStudyType(),
                    row.getRecordStartedAt(),
                    row.getQuestionsSolved(),
                    row.getQuestionsIncorrected(),
                    row.getQuestionsPercent(),
                    row.getRecordAnnotation()
            ));

        }

        return new ArrayList<>(topicsMap.values());
    }
}
