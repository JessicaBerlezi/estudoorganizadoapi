package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.HistoryDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyStructureDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicStructureDTO;
import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import br.pucrs.estudoorganizado.entity.view.StudyStructureView;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.LocalDateTime;
import java.util.Optional;

public class StudyStructureViewMapper {

    private static String buildStatusInfo(LocalDateTime startedAt) {
        return startedAt == null ? "Não iniciado" : "Iniciado em " + startedAt;
    }


    public static StudyStructureDTO buildStudyCycleInfo(StudyStructureView row, int order) {
        StudyStructureDTO dto = new StudyStructureDTO();
        dto.setId(row.getStudyCycleId());
        dto.setOrder((long) order);
        dto.setDescription(row.getCycleDescription());
        dto.setAnnotation(row.getCycleAnnotation());
        dto.setStatusInfo(buildStatusInfo(row.getCycleStartedAt()));
        return dto;
    }


    public static StudyStructureDTO buildStudyCycleReviewInfo(StudyStructureView row, int order) {
        StudyStructureDTO dto = new StudyStructureDTO();
        dto.setId(row.getTopicId());
        dto.setOrder((long) order);
        boolean hasDelayed = row.getReviewStatus() == ReviewStatusEnum.DELAYED;
        dto.setDescription(hasDelayed ? "Revisão em atraso" : "Revisão planejada");
        dto.setStatusInfo("Data planejada: " + row.getReviewScheduleDate().format(Utils.DATE_FMT));
        return dto;
    }


    public static StudyStructureDTO buildSubjectInfo(StudyStructureView row) {
        StudyStructureDTO dto = new StudyStructureDTO();
        dto.setId(row.getSubjectId());
        dto.setOrder(row.getSubjectOrder());
        dto.setDescription(row.getSubjectDescription());
        dto.setAnnotation(row.getSubjectAnnotation());
        dto.setStatusInfo(buildStatusInfo(row.getCycleStartedAt()));
        return dto;
    }


    public static TopicStructureDTO buildTopicInfo(StudyStructureView row) {
        TopicStructureDTO dto = new TopicStructureDTO();
        dto.setId(row.getTopicId());
        dto.setOrder(row.getTopicOrder());
        dto.setColor(Utils.resolveTopicColor(row.getTopicIncidenceScore(), row.getTopicKnowledgeScore()));
        dto.setDescription(row.getTopicDescription());
        dto.setSubject(row.getSubjectDescription());
        dto.setAnnotation(row.getTopicAnnotation());

        long topicElapsedTime = 0L;
        long topicScoreAvg = 0L;
        if(row.getStudyRecordId() != null){
            if(row.getStudyDurationMinutes() != null) {
                topicElapsedTime += row.getStudyDurationMinutes();
            }
            int solved = Optional.ofNullable(row.getStudyQuestionsSolved()).orElse(0);
            int incorrect = Optional.ofNullable(row.getStudyQuestionsIncorrected()).orElse(0);

            int correct = Math.max(0, solved - incorrect);
            long avg = Math.round(((double) correct / solved) * 100);
            topicScoreAvg += avg;

            HistoryDTO history = new HistoryDTO();
            history.information = (solved + "Q " + correct + "A " + avg + "%");


            String label = switch (row.getStudyType()) {
                case STUDY_CYCLE -> "Estudo";
                case REVIEW -> "Revisão";
            };

            history.description = (label + " "
                    + Utils.formatDurationMinutes(row.getStudyDurationMinutes()) +" em "
                    + row.getStudyStartedAt().format(Utils.DATE_FMT));

            history.annotation = row.getStudyAnnotation();

            dto.getHistory().add(history);
        } else {
            dto.setElapsedTime("0 min");
            dto.setScore("- %");
        }

        dto.setElapsedTime(topicElapsedTime == 0 ? "0 min" : Utils.formatDurationMinutes(topicElapsedTime));
        dto.setScore(topicScoreAvg == 0 ? "- %" : topicScoreAvg + "%");

        return dto;
    }

}
