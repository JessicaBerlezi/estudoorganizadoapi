package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.controller.dto.RegistreStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import br.pucrs.estudoorganizado.entity.map.StudyRecordMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class StudyRecordMapperTest {

    @Test
    void shouldToStudyRecordCorrectlyWhenDataIsValid() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.of(2026, 2, 6),
                30L,
                10,
                2,
                "Sessão produtiva",
                false
        );

        StudyRecordEntity result = StudyRecordMapper.toStudyRecord(item, dto);

        assertEquals(item, result.getStudyCycleItem());
        assertEquals(StudyTypeEnum.STUDY_CYCLE, result.getStudyType());
        assertEquals(LocalDate.of(2026, 2, 6), result.getStartedAt());
        assertEquals(30L, result.getMinutes()); // agora compara com Long
        assertEquals(10, result.getQuestionsSolved());
        assertEquals(2, result.getQuestionsIncorrected());
        assertEquals("Sessão produtiva", result.getAnnotation());
    }

    @Test
    void shouldCalculateQuestionsPercentCorrectly() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                20L,
                8,
                2,
                null,
                false
        );

        StudyRecordEntity result = StudyRecordMapper.toStudyRecord(item, dto);

        // 6 corretas de 8 = 75%
        assertEquals(75.0, result.getQuestionsPercent());
    }

    @Test
    void shouldReturnZeroPercentWhenNoQuestionsSolved() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                15L,
                0,
                0,
                null,
                false
        );

        StudyRecordEntity result = StudyRecordMapper.toStudyRecord(item, dto);

        assertEquals(0.0, result.getQuestionsPercent());
    }

    @Test
    void shouldReturnZeroDurationWhenMinutesIsNull() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                null,
                5,
                1,
                null,
                false
        );

        StudyRecordEntity result = StudyRecordMapper.toStudyRecord(item, dto);

        assertEquals(0L, result.getMinutes()); // null vira 0
    }

    @Test
    void shouldReturnZeroWhenMinutesIsZeroOrNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();

        RegistreStudyRecordDTO zeroDto = new RegistreStudyRecordDTO(
                LocalDate.now(), 0L, 5, 1, null, false
        );

        RegistreStudyRecordDTO negativeDto = new RegistreStudyRecordDTO(
                LocalDate.now(), -10L, 5, 1, null, false
        );

        assertEquals(0L, StudyRecordMapper.toStudyRecord(item, zeroDto).getMinutes());
        assertEquals(0L, StudyRecordMapper.toStudyRecord(item, negativeDto).getMinutes());
    }

    @Test
    void shouldThrowExceptionWhenSolvedIsNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                10L,
                -1,
                0,
                null,
                false
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.toStudyRecord(item, dto));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectIsNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                10L,
                5,
                -1,
                null,
                false
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.toStudyRecord(item, dto));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectGreaterThanSolved() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        RegistreStudyRecordDTO dto = new RegistreStudyRecordDTO(
                LocalDate.now(),
                10L,
                5,
                6,
                null,
                false
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.toStudyRecord(item, dto));
    }
}
