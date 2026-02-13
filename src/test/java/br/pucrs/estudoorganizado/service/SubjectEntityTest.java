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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

       InsertTopicDTO topic1 = new InsertTopicDTO();
        topic1.description = "Topico 1";
        topic1.incidenceScore = 1;
        topic1.knowledgeScore = 1;
        topic1.reviewIntervals = List.of(1, 5);

        InsertTopicDTO topic2 = new InsertTopicDTO();
        topic2.description = "Topico 2";
        topic2.incidenceScore = 2;
        topic2.knowledgeScore = 2;
        topic2.reviewIntervals = List.of(2, 10);


        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Disciplina Teste";
        dto.annotation = null;
        dto.topics = List.of(topic1, topic2);


        SubjectDTO result = subjectService.createSubjectWithTopics(dto);

        SubjectEntity saved =
                subjectRepository.findById(result.getId()).orElseThrow();

        Assertions.assertEquals("Disciplina Teste", saved.getDescription());
        Assertions.assertTrue(saved.getIsActive());
        Assertions.assertEquals(2, saved.getTopics().size());
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingSubject() {

        SubjectEntity entity = new SubjectEntity();

        Assertions.assertThrows(ResponseStatusException.class,
                () -> subjectService.updateSubjectWithTopics(entity));
    }

    @Test
    void shouldReturnOnlyActiveSubjects() {

        SubjectEntity active = Mocks.createSubjectEntityMock();
        subjectRepository.save(active);

        SubjectEntity inactive = Mocks.createSubjectEntityMock();
        inactive.setIsActive(false);
        subjectRepository.save(inactive);

        List<SubjectDTO> result =
                subjectService.findActivesSubjects();
        System.out.println("TOTAL: " + result.size());
        result.forEach(s -> System.out.println(s.getDescription()));

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void shouldThrowPreconditionFailedWhenSubjectInactive() {

        SubjectEntity subject = Mocks.createSubjectEntityMock();
        subject.setIsActive(false);
        subjectRepository.save(subject);

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> subjectService.getActiveSubject(subject.getId()));

        Assertions.assertEquals(HttpStatus.CONFLICT,
                ex.getStatusCode());
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
