package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import br.pucrs.estudoorganizado.entity.map.StudyRecordMapper;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class StudyRecordMapperTest {


    @Test
    void shouldMapCorrectlyWhenDataIsValid() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.of(2026, 2, 6),
                30L,
                10,
                2,
                "Sessão produtiva"
        );

        StudyRecordEntity result = StudyRecordMapper.map(item, dto);

        assertEquals(item, result.getStudyCycleItem());
        assertEquals(StudyTypeEnum.STUDY_CYCLE, result.getStudyType());
        assertEquals(LocalDate.of(2026, 2, 6), result.getStartedAt());
        assertEquals(Duration.ofMinutes(30), result.getDurationMinutes());
        assertEquals(10, result.getQuestionsSolved());
        assertEquals(2, result.getQuestionsIncorrected());
        assertEquals("Sessão produtiva", result.getAnnotation());
    }

    @Test
    void shouldCalculateQuestionsPercentCorrectly() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                20L,
                8,
                2,
                null
        );

        StudyRecordEntity result = StudyRecordMapper.map(item, dto);

        // 6 corretas de 8 = 75%
        assertEquals(75.0, result.getQuestionsPercent());
    }

    @Test
    void shouldReturnZeroPercentWhenNoQuestionsSolved() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                15L,
                0,
                0,
                null
        );

        StudyRecordEntity result = StudyRecordMapper.map(item, dto);

        assertEquals(0.0, result.getQuestionsPercent());
    }

    @Test
    void shouldReturnZeroDurationWhenMinutesIsNull() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                null,
                5,
                1,
                null
        );

        StudyRecordEntity result = StudyRecordMapper.map(item, dto);

        assertEquals(Duration.ZERO, result.getDurationMinutes());
    }

    @Test
    void shouldReturnZeroDurationWhenMinutesIsZeroOrNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();

        InsertStudyRecordDTO zeroDto = new InsertStudyRecordDTO(
                LocalDate.now(), 0L, 5, 1, null
        );

        InsertStudyRecordDTO negativeDto = new InsertStudyRecordDTO(
                LocalDate.now(), -10L, 5, 1, null
        );

        assertEquals(Duration.ZERO, StudyRecordMapper.map(item, zeroDto).getDurationMinutes());
        assertEquals(Duration.ZERO, StudyRecordMapper.map(item, negativeDto).getDurationMinutes());
    }

    @Test
    void shouldThrowExceptionWhenSolvedIsNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                10L,
                -1,
                0,
                null
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.map(item, dto));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectIsNegative() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                10L,
                5,
                -1,
                null
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.map(item, dto));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectGreaterThanSolved() {
        StudyCycleItemEntity item = new StudyCycleItemEntity();
        InsertStudyRecordDTO dto = new InsertStudyRecordDTO(
                LocalDate.now(),
                10L,
                5,
                6,
                null
        );

        assertThrows(IllegalArgumentException.class,
                () -> StudyRecordMapper.map(item, dto));
    }
}
