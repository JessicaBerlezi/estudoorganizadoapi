package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.RegistreStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.ReviewControlEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.StudyRecordEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;

public class StudyRecordMapper {

    public static StudyRecordEntity toStudyRecord(StudyCycleItemEntity studyCycleItem, RegistreStudyRecordDTO dto) {
        return buildRecord(studyCycleItem, null, StudyTypeEnum.STUDY_CYCLE, dto);
    }

    public static StudyRecordEntity toReviewRecord(ReviewControlEntity reviewControl, RegistreStudyRecordDTO dto) {
        return buildRecord(null, reviewControl, StudyTypeEnum.REVIEW, dto);
    }

    private static StudyRecordEntity buildRecord(StudyCycleItemEntity studyCycleItem, ReviewControlEntity reviewControl, StudyTypeEnum type, RegistreStudyRecordDTO dto) {

            int solved = dto.getQuestionsSolved();
        int incorrect = dto.getQuestionsIncorrected();

        if (solved < 0 || incorrect < 0) {
            throw new IllegalArgumentException("Quantidade de questões não pode ser negativa.");
        }

        if (incorrect > solved) {
            throw new IllegalArgumentException("Questões incorretas não podem ser maiores que questões resolvidas.");
        }

        Long minutes = dto.getMinutes();
        if (minutes == null || minutes <= 0) {
            minutes = 0L;
        }

        return new StudyRecordEntity(
                studyCycleItem,
                reviewControl,
                type,
                dto.getStartedAt(),
                minutes,
                calcQuestionsPercent(dto),
                solved,
                incorrect,
                dto.getAnnotation()
        );
    }

    private static double calcQuestionsPercent(RegistreStudyRecordDTO dto) {
        int solved = dto.getQuestionsSolved();
        int incorrect = dto.getQuestionsIncorrected();

        if (solved == 0) return 0.0;

        int correct = solved - incorrect;
        return (double) correct / solved * 100.0;
    }
}
