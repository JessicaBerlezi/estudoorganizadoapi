package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.Mocks;
import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.service.StudyMapService;
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
    private StudyMapService service;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private StudyMapComponent component;


    @Test
    void shouldReturnStudyMap() {
        StudyMapDTO dto = new StudyMapDTO();

        when(service.getStudyMaps()).thenReturn(dto);

        StudyMapDTO result = component.getStudyMap();

        Assertions.assertEquals(dto, result);
        verify(service).getStudyMaps();
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenGetStudyMapFailsWithResponseStatus() {
        when(service.getStudyMaps())
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro"));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> component.getStudyMap());

        verify(service).getStudyMaps();
    }

    @Test
    void shouldThrowBadRequestWhenGetStudyMapFailsWithGenericException() {
        when(service.getStudyMaps())
                .thenThrow(new RuntimeException("Erro interno"));

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.getStudyMap());

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals("Erro ao carregar mapa de estudos.", ex.getReason());
    }

    // ================================
    // createSubjectWithTopics
    // ================================

    @Test
    void shouldCreateSubject() {
        InsertSubjectDTO insertDto = new InsertSubjectDTO();
        SubjectDTO expected = new SubjectDTO();

        when(subjectService.createSubjectWithTopics(insertDto))
                .thenReturn(expected);

        SubjectDTO result = component.createSubjectWithTopics(insertDto);

        Assertions.assertEquals(expected, result);
        verify(subjectService).createSubjectWithTopics(insertDto);
    }

    @Test
    void shouldThrowBadRequestWhenCreateFails() {
        InsertSubjectDTO insertDto = new InsertSubjectDTO();

        when(subjectService.createSubjectWithTopics(insertDto))
                .thenThrow(new RuntimeException());

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.createSubjectWithTopics(insertDto));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals(
                "Erro ao criar nova disciplina no mapa de estudos.",
                ex.getReason()
        );
    }


    // ================================
    // getSubjectById
    // ================================

    @Test
    void shouldReturnSubjectById() {
        SubjectEntity entity = Mocks.createSubjectEntityMock();

        when(subjectService.getActiveSubject(1L))
                .thenReturn(entity);

        SubjectDTO result = component.getSubjectById(1L);

        SubjectDTO dto = SubjectMapper.convertToDTO(entity);
        Assertions.assertEquals(dto.getTopics().size(), result.getTopics().size());
        verify(subjectService).getActiveSubject(1L);
    }

    @Test
    void shouldThrowBadRequestWhenGetSubjectFails() {
        when(subjectService.getActiveSubject(1L))
                .thenThrow(new RuntimeException());

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.getSubjectById(1L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals(
                "Erro ao carregar informações de disciplinas.",
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
        SubjectDTO result = component.updateSubjectWithTopics(subjectId, dto);

        // Assert
        Assertions.assertNotNull(result);

        // Captura o entity REAL que foi salvo
        ArgumentCaptor<SubjectEntity> captor = ArgumentCaptor.forClass(SubjectEntity.class);
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

        Assertions.assertEquals("Erro ao atualizar dados de disciplinas.", ex.getReason());
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
