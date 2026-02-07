package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyRecordDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicHistoryDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.StudyRecordEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;

import java.time.Duration;

public class StudyRecordMapper {

    public static StudyRecordEntity map(StudyCycleItemEntity item, InsertStudyRecordDTO dto) {

        int solved = dto.getQuestionsSolved();
        int incorrect = dto.getQuestionsIncorrected();

        if (solved < 0 || incorrect < 0) {
            throw new IllegalArgumentException("Quantidade de questões não pode ser negativa.");
        }

        if (incorrect > solved) {
            throw new IllegalArgumentException("Questões incorretas não podem ser maiores que questões resolvidas.");
        }

        return new StudyRecordEntity(
                item,
                StudyTypeEnum.STUDY_CYCLE,
                dto.getStartedAt(),
                dto.getMinutes(),
                calcQuestionsPercent(dto),
                solved,
                incorrect,
                dto.getAnnotation()
        );
    }

    private static double calcQuestionsPercent(InsertStudyRecordDTO dto) {
        int solved = dto.getQuestionsSolved();
        int incorrect = dto.getQuestionsIncorrected();

        if (solved == 0) return 0.0;

        int correct = solved - incorrect;
        return (double) correct / solved * 100.0;
    }


    public static TopicHistoryDTO toHistoryDTO(StudyRecordEntity entity) {

        TopicHistoryDTO dto = new TopicHistoryDTO();

        // ===== Description
        String label = switch (entity.getStudyType()) {
            case STUDY_CYCLE -> "Questões";
            case REVIEW -> "Revisão";
            default -> "Estudo";
        };

        String date = entity.getStartedAt()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        dto.description = label + " " + date;

        // ===== Information
        int solved = entity.getQuestionsSolved() != null ? entity.getQuestionsSolved() : 0;
        int incorrect = entity.getQuestionsIncorrected() != null ? entity.getQuestionsIncorrected() : 0;
        int correct = Math.max(0, solved - incorrect);

        String percent = Math.round(entity.getQuestionsPercent()) + "%";

        dto.information = solved + "Q " + correct + "A " + percent;

        // ===== Annotation
        dto.annotation = entity.getAnnotation();

        return dto;
    }
}
