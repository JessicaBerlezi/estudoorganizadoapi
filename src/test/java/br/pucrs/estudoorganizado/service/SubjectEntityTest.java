package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.Mocks;
import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class SubjectEntityTest {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void cleanDatabase() {
        subjectRepository.deleteAll();
    }

    @Test
    void shouldCreateSubjectWithTopics() {

       InsertTopicStructureDTO topic1 = new InsertTopicStructureDTO();
        topic1.description = "Topico 1";
        topic1.incidenceScore = 1;
        topic1.knowledgeScore = 1;
        topic1.reviewIntervals = List.of(1, 5);

        InsertTopicStructureDTO topic2 = new InsertTopicStructureDTO();
        topic2.description = "Topico 2";
        topic2.incidenceScore = 2;
        topic2.knowledgeScore = 2;
        topic2.reviewIntervals = List.of(2, 10);


        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Disciplina Teste";
        dto.annotation = null;
        dto.topics = List.of(topic1, topic2);

        SubjectEntity result = subjectService.createSubjectWithTopics(dto);

        // ASSERT retorno
        Assertions.assertNotNull(result.getId());

        // ASSERT banco real
        SubjectEntity saved =
                subjectRepository.findById(result.getId()).orElseThrow();

        Assertions.assertEquals("Disciplina Teste", saved.getDescription());
        Assertions.assertTrue(saved.getIsActive());
        Assertions.assertEquals(2, saved.getTopics().size());
    }


    @Test
    void shouldCreateSubjectSuccessfullyWithTopics() {
        // Arrange
        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Álgebra";
        topic.incidenceScore = 3;
        topic.knowledgeScore = 2;
        topic.reviewIntervals = List.of(1, 3, 7);

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Matemática";
        dto.annotation = "Base";
        dto.topics = List.of(topic);

        // Act
        SubjectEntity entity = subjectService.createSubjectWithTopics(dto);

        // Assert
        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(entity.getId());
        Assertions.assertEquals("Matemática", entity.getDescription());
        Assertions.assertTrue(entity.getIsActive());
        Assertions.assertEquals(1, entity.getTopics().size());
    }

    @Test
    void shouldPersistTopicsCascadeCorrectly() {
        // Arrange
        InsertTopicStructureDTO t1 = new InsertTopicStructureDTO();
        t1.description = "Topico 1";
        t1.incidenceScore = 1;
        t1.knowledgeScore = 1;
        t1.reviewIntervals = List.of(1, 5);

        InsertTopicStructureDTO t2 = new InsertTopicStructureDTO();
        t2.description = "Topico 2";
        t2.incidenceScore = 2;
        t2.knowledgeScore = 2;
        t2.reviewIntervals = List.of(2, 10);

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Disciplina Cascade";
        dto.annotation = "Teste cascade";
        dto.topics = List.of(t1, t2);

        // Act
        SubjectEntity saved = subjectService.createSubjectWithTopics(dto);

        // Reload do banco (importante)
        SubjectEntity fromDb = subjectRepository.findById(saved.getId()).orElseThrow();

        // Assert
        Assertions.assertEquals(2, fromDb.getTopics().size());
        Assertions.assertTrue(
                fromDb.getTopics().stream()
                        .allMatch(topic -> topic.getId() != null),
                "Tópicos devem ser persistidos com ID"
        );
    }


    @Test
    void shouldHandleNullReviewIntervals() {
        // Arrange
        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Topico sem revisão";
        topic.incidenceScore = 1;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = null; // vindo null do front

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Teste Review Null";
        dto.topics = List.of(topic);

        // Act
        SubjectEntity entity = subjectService.createSubjectWithTopics(dto);

        // Assert
        Assertions.assertNotNull(entity.getId());
        Assertions.assertEquals(1, entity.getTopics().size());
    }

    @Test
    void shouldPersistAnnotationAsNullWithoutBreaking() {
        // Arrange
        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Topico";
        topic.incidenceScore = 1;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1);

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Annotation Null";
        dto.annotation = null; // campo opcional
        dto.topics = List.of(topic);

        // Act
        SubjectEntity entity = subjectService.createSubjectWithTopics(dto);

        // Assert
        SubjectEntity fromDb = subjectRepository.findById(entity.getId()).orElseThrow();
        Assertions.assertNull(fromDb.getAnnotation());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        // Arrange
        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = null; // inválido
        dto.topics = new ArrayList<>();

        // Act & Assert
        Assertions.assertThrows(Exception.class, () ->
                subjectService.createSubjectWithTopics(dto)
        );
    }
    @Test
    void shouldBreakWhenTopicFieldsAreInvalidsNull() {
        // Arrange
        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = null;
        topic.incidenceScore = -2;
        topic.knowledgeScore = 3;
        topic.reviewIntervals = null;

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Teste campos inválidos";
        dto.topics = List.of(topic);

        // Act & Assert
        Assertions.assertThrows(ResponseStatusException.class, () ->
                subjectService.createSubjectWithTopics(dto)
        );
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingSubject() {

        SubjectEntity entity = new SubjectEntity();

        Assertions.assertThrows(ResponseStatusException.class,
                () -> subjectService.updateSubjectWithTopics(entity));
    }

    @Test
    @Transactional
    void shouldDisableSubjectAndTopicsAndUpdateTimestamp() {

        SubjectEntity subject = Mocks.createSubjectEntityMock();
        subject.setCreatedAt(LocalDateTime.now().minusHours(2));
        subject = subjectRepository.save(subject);

        subjectService.disableSubject(subject.getId());

        SubjectEntity updated = subjectRepository.findByIdWithTopics(subject.getId()).orElseThrow();


        // Subject desativado
        Assertions.assertFalse(updated.getIsActive());

        // Todos os tópicos desativados
        updated.getTopics()
                .forEach(topic ->
                        Assertions.assertFalse(topic.getIsActive())
                );

        // updatedAt diferente de createdAt
        Assertions.assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());

        // updatedAt maior que createdAt
        Assertions.assertTrue(
                updated.getUpdatedAt().isAfter(updated.getCreatedAt())
        );
    }
}
