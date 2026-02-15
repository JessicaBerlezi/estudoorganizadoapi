package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.controller.dto.StudyStructureDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicStructureDTO;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import br.pucrs.estudoorganizado.entity.map.StudyStructureViewMapper;
import br.pucrs.estudoorganizado.entity.view.StudyStructureView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class StudyStructureViewMapperTest {


    private StudyStructureView buildBaseRow() {
        StudyStructureView row = new StudyStructureView();
        row.setSubjectId(1L);
        row.setSubjectDescription("Matemática");
        row.setSubjectAnnotation("Base");

        row.setTopicId(10L);
        row.setTopicDescription("Álgebra");
        row.setTopicAnnotation("Importante");
        row.setTopicOrder(1L);

        row.setTopicIncidenceScore(5);
        row.setTopicKnowledgeScore(3);

        row.setCycleDescription("Ciclo 1");
        row.setCycleAnnotation("Anotação ciclo");
        row.setStudyCycleId(100L);

        return row;
    }

    @Test
    @DisplayName("Deve mapear subject mesmo com cycleStartedAt null")
    void shouldMapSubjectWithNullStartedAt() {
        StudyStructureView row = buildBaseRow();
        row.setCycleStartedAt(null);

        StudyStructureDTO dto = StudyStructureViewMapper.buildSubjectInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("Matemática", dto.getDescription());
        Assertions.assertEquals("Não iniciado", dto.getStatusInfo());
    }

    @Test
    @DisplayName("Deve mapear cycle mesmo com campos nulos")
    void shouldMapCycleWithNullFields() {
        StudyStructureView row = new StudyStructureView();
        row.setStudyCycleId(1L);
        row.setCycleDescription(null);
        row.setCycleAnnotation(null);
        row.setCycleStartedAt(null);

        StudyStructureDTO dto = StudyStructureViewMapper.buildStudyCycleInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("Não iniciado", dto.getStatusInfo());
    }

    @Test
    @DisplayName("Não deve quebrar quando não há studyRecord (LEFT JOIN null)")
    void shouldHandleNullStudyRecordGracefully() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(null);

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("0 min", dto.getElapsedTime());
        Assertions.assertEquals("- %", dto.getScore());
        Assertions.assertTrue(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Não deve quebrar com duration null")
    void shouldHandleNullDuration() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(null);
        row.setStudyQuestionsSolved(10);
        row.setStudyQuestionsIncorrected(2);
        row.setStudyType(StudyTypeEnum.STUDY_CYCLE);
        row.setStudyStartedAt(LocalDate.now());

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("0 min", dto.getElapsedTime());
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Não deve quebrar com solved null")
    void shouldHandleNullSolvedQuestions() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(30L);
        row.setStudyQuestionsSolved(null);
        row.setStudyQuestionsIncorrected(null);
        row.setStudyType(StudyTypeEnum.REVIEW);
        row.setStudyStartedAt(LocalDate.now());

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("30 min", dto.getElapsedTime());
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Não deve quebrar com campos de resolucao de cor null")
    void shouldHandleNullKnowledgeScore() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(30L);
        row.setStudyQuestionsSolved(null);
        row.setStudyQuestionsIncorrected(null);
        row.setTopicKnowledgeScore(null);
        row.setStudyType(StudyTypeEnum.REVIEW);
        row.setStudyStartedAt(LocalDate.now());

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("30 min", dto.getElapsedTime());
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Não deve quebrar com campos de resolucao de cor null")
    void shouldHandleNullIncidenceScore() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(30L);
        row.setStudyQuestionsSolved(null);
        row.setStudyQuestionsIncorrected(null);
        row.setTopicKnowledgeScore(0);
        row.setTopicIncidenceScore(null);
        row.setStudyType(StudyTypeEnum.REVIEW);
        row.setStudyStartedAt(LocalDate.now());

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("30 min", dto.getElapsedTime());
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Deve detectar divisão por zero (solved = 0)")
    void shouldNotCrashWhenSolvedIsZero() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(20L);
        row.setStudyQuestionsSolved(0);
        row.setStudyQuestionsIncorrected(0);
        row.setStudyType(StudyTypeEnum.STUDY_CYCLE);
        row.setStudyStartedAt(LocalDate.now());

        Assertions.assertDoesNotThrow(() ->
                StudyStructureViewMapper.buildTopicInfo(row)
        );
    }

    @Test
    @DisplayName("Deve quebrar hoje se studyType for null (teste de ponto de falha real)")
    void shouldExposeBreakPointWhenStudyTypeIsNull() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(10L);
        row.setStudyQuestionsSolved(5);
        row.setStudyQuestionsIncorrected(1);
        row.setStudyType(null); // ← PONTO CRÍTICO
        row.setStudyStartedAt(LocalDate.now());

        Assertions.assertThrows(NullPointerException.class, () ->
                StudyStructureViewMapper.buildTopicInfo(row)
        );
    }

    @Test
    @DisplayName("Deve quebrar hoje se studyStartedAt for null (outro ponto crítico)")
    void shouldExposeBreakPointWhenStudyStartedAtIsNull() {
        StudyStructureView row = buildBaseRow();
        row.setStudyRecordId(1L);
        row.setStudyDurationMinutes(15L);
        row.setStudyQuestionsSolved(10);
        row.setStudyQuestionsIncorrected(2);
        row.setStudyType(StudyTypeEnum.STUDY_CYCLE);
        row.setStudyStartedAt(null); // ← PONTO CRÍTICO

        Assertions.assertThrows(NullPointerException.class, () ->
                StudyStructureViewMapper.buildTopicInfo(row)
        );
    }

    @Test
    @DisplayName("Teste com todos os campos nulos (simulação extrema da VIEW)")
    void shouldNotCrashWithAllNullDatabaseFields() {
        StudyStructureView row = new StudyStructureView();

        Assertions.assertDoesNotThrow(() -> {
            StudyStructureViewMapper.buildSubjectInfo(row);
            StudyStructureViewMapper.buildStudyCycleInfo(row);
        });
    }


    private StudyStructureView buildRowWithHistoryBase() {
        StudyStructureView row = new StudyStructureView();
        row.setSubjectDescription("Matemática");
        row.setTopicId(1L);
        row.setTopicOrder(1L);
        row.setTopicDescription("Álgebra");
        row.setTopicAnnotation("Anotação");

        row.setTopicIncidenceScore(5);
        row.setTopicKnowledgeScore(3);

        // Força criação do HistoryDTO
        row.setStudyRecordId(100L);

        return row;
    }

    @Test
    @DisplayName("Deve criar HistoryDTO mesmo com annotation null")
    void shouldHandleNullStudyAnnotation() {
        StudyStructureView row = buildRowWithHistoryBase();
        row.setStudyType(StudyTypeEnum.STUDY_CYCLE);
        row.setStudyDurationMinutes(30L);
        row.setStudyStartedAt(LocalDate.now());
        row.setStudyQuestionsSolved(10);
        row.setStudyQuestionsIncorrected(2);
        row.setStudyAnnotation(null); // NULL vindo do banco

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getHistory().size());
        Assertions.assertNull(dto.getHistory().get(0).annotation);
    }

    @Test
    @DisplayName("Não deve quebrar com duration null no HistoryDTO")
    void shouldHandleNullDurationInHistory() {
        StudyStructureView row = buildRowWithHistoryBase();
        row.setStudyType(StudyTypeEnum.REVIEW);
        row.setStudyDurationMinutes(null); // NULL do banco
        row.setStudyStartedAt(LocalDate.now());
        row.setStudyQuestionsSolved(5);
        row.setStudyQuestionsIncorrected(1);

        Assertions.assertDoesNotThrow(() ->
                StudyStructureViewMapper.buildTopicInfo(row)
        );

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Não deve quebrar com duration null no HistoryDTO")
    void shouldHandleNullQuestionsSolvedInHistory() {
        StudyStructureView row = buildRowWithHistoryBase();
        row.setStudyType(StudyTypeEnum.REVIEW);
        row.setStudyDurationMinutes(null); // NULL do banco
        row.setStudyStartedAt(LocalDate.now());
        row.setStudyQuestionsSolved(null);
        row.setStudyQuestionsIncorrected(1);

        Assertions.assertDoesNotThrow(() ->
                StudyStructureViewMapper.buildTopicInfo(row)
        );

        TopicStructureDTO dto = StudyStructureViewMapper.buildTopicInfo(row);
        Assertions.assertFalse(dto.getHistory().isEmpty());
    }

}
