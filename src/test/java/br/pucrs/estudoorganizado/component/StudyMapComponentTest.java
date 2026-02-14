package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.Mocks;
import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.service.StudyStructureViewService;
import br.pucrs.estudoorganizado.service.SubjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class StudyMapComponentTest {
    @Mock
    private StudyStructureViewService viewService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private StudyMapComponent component;


    @Test
    void shouldReturnStudyMap() {
        List<StudyStructureDTO> dtos = new ArrayList<>();

        when(viewService.findActiveSubjectsWithFullTopicHistory()).thenReturn(dtos);

        StudyMapStructureDTO result = component.buildStudyMapInfo();

        Assertions.assertEquals(dtos, result.subjects);
        verify(viewService, times(1)).findActiveSubjectsWithFullTopicHistory();
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenBuildStudyMapInfoFailsWithResponseStatus() {
        when(viewService.findActiveSubjectsWithFullTopicHistory())
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro"));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> component.buildStudyMapInfo());

        verify(viewService, times(1)).findActiveSubjectsWithFullTopicHistory();
    }

    @Test
    void shouldThrowBadRequestWhenBuildStudyMapInfoFailsWithGenericException() {
        when(viewService.findActiveSubjectsWithFullTopicHistory())
                .thenThrow(new RuntimeException("Erro interno"));

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.buildStudyMapInfo());

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
        Assertions.assertEquals(BusinessError.STUDY_MAPS_LOAD.message(), ex.getReason());
    }


    @Test
    void shouldThrowBadRequestWhenGetSubjectFails() {
        when(viewService.getActiveSubjectWithFullTopicHistory(1L))
                .thenThrow(new RuntimeException());

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.getSubjectById(1L));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
        Assertions.assertEquals(
                BusinessError.STUDY_MAP_LOAD.message(),
                ex.getReason()
        );
    }

    // ================================
    // updateSubjectWithTopics
    // ================================


    @Test
    void shouldUpdateExistingTopicAddNewAndRemoveOrphan() {
        // Arrange
        Long subjectId = 1L;
        SubjectEntity subjectMock = Mocks.createSubjectEntityMockWithId();
        subjectMock.setTopics(new ArrayList<>(subjectMock.getTopics()));

        UpdateTopicDTO updateExisting = new UpdateTopicDTO();
        updateExisting.id = 101L;
        updateExisting.description = "Verbos Atualizado";

        UpdateTopicDTO newTopic = new UpdateTopicDTO();
        newTopic.id = null;
        newTopic.description = "Novo Tópico";

        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.topics = List.of(updateExisting, newTopic);

        when(subjectService.getActiveSubject(subjectId))
                .thenReturn(subjectMock);

        when(subjectService.updateSubjectWithTopics(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        component.updateSubjectWithTopics(subjectId, dto);
        ArgumentCaptor<SubjectEntity> captor =
                ArgumentCaptor.forClass(SubjectEntity.class);

        verify(subjectService).updateSubjectWithTopics(captor.capture());

        SubjectEntity savedEntity = captor.getValue();
        List<TopicEntity> topics = savedEntity.getTopics();

        // Deve remover o órfão (102L)
        Assertions.assertTrue(
                topics.stream().noneMatch(t -> Long.valueOf(102L).equals(t.getId()))
        );

        // Deve manter o 101 atualizado
        Assertions.assertTrue(
                topics.stream().anyMatch(t ->
                        Long.valueOf(101L).equals(t.getId()))
        );

        // Deve adicionar novo tópico (id null ou novo id)
        Assertions.assertEquals(2, topics.size());

        verify(subjectService).getActiveSubject(subjectId);
    }

    @Test
    void shouldRemoveAllTopicsWhenDtoTopicsIsEmpty() {
        // Arrange
        Long subjectId = 1L;
        SubjectEntity subjectMock = Mocks.createSubjectEntityMockWithId();
        subjectMock.setTopics(new ArrayList<>(subjectMock.getTopics()));

        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.topics = new ArrayList<>();

        when(subjectService.getActiveSubject(subjectId))
                .thenReturn(subjectMock);

        when(subjectService.updateSubjectWithTopics(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        component.updateSubjectWithTopics(subjectId, dto);

        // Assert
        ArgumentCaptor<SubjectEntity> captor = ArgumentCaptor.forClass(SubjectEntity.class);
        verify(subjectService).updateSubjectWithTopics(captor.capture());

        SubjectEntity saved = captor.getValue();

        Assertions.assertTrue(saved.getTopics().isEmpty(),
                "Todos os tópicos deveriam ser removidos quando a lista vem vazia");
    }


    @Test
    void shouldOnlyUpdateWhenAllTopicsExist() {
        // Arrange
        Long subjectId = 1L;
        SubjectEntity subjectMock = Mocks.createSubjectEntityMockWithId();
        subjectMock.setTopics(new ArrayList<>(subjectMock.getTopics()));

        UpdateTopicDTO t1 = new UpdateTopicDTO();
        t1.id = 101L;
        t1.description = "Verbos Editado";

        UpdateTopicDTO t2 = new UpdateTopicDTO();
        t2.id = 102L;
        t2.description = "Pronomes Editado";

        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.topics = List.of(t1, t2);

        when(subjectService.getActiveSubject(subjectId))
                .thenReturn(subjectMock);

        when(subjectService.updateSubjectWithTopics(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        component.updateSubjectWithTopics(subjectId, dto);

        // Assert
        ArgumentCaptor<SubjectEntity> captor = ArgumentCaptor.forClass(SubjectEntity.class);
        verify(subjectService).updateSubjectWithTopics(captor.capture());

        SubjectEntity saved = captor.getValue();

        Assertions.assertEquals(2, saved.getTopics().size(),
                "Não deveria remover nem adicionar tópicos");
    }


    @Test
    void shouldThrowBadRequestWhenUnexpectedExceptionOccurs() {
        // Arrange
        Long subjectId = 1L;
        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.topics = new ArrayList<>();

        when(subjectService.getActiveSubject(subjectId))
                .thenThrow(new RuntimeException("Erro inesperado"));

        // Act + Assert
        ResponseStatusException ex = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> component.updateSubjectWithTopics(subjectId, dto)
        );

        Assertions.assertEquals(BusinessError.STUDY_MAP_UPDATE.message(), ex.getReason());
        verify(subjectService).getActiveSubject(subjectId);
    }

    // ================================
    // deleteSubject
    // ================================

    @Test
    void shouldDisableSubject() {
        component.disableSubject(1L);
        verify(subjectService).disableSubject(1L);
    }

    @Test
    void shouldThrowBadRequestWhenDisableFails() {

        doThrow(new RuntimeException())
                .when(subjectService)
                .disableSubject(1L);

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.disableSubject(1L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals(
                BusinessError.SUBJECT_DISABLE.message(),
                ex.getReason()
        );
    }

}
