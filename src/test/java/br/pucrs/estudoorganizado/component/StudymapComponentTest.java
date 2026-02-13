package br.pucrs.estudoorganizado.component;


import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyMapDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectDTO;
import br.pucrs.estudoorganizado.service.StudyMapService;
import br.pucrs.estudoorganizado.service.SubjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class StudymapComponentTest {
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
        SubjectDTO dto = new SubjectDTO();

        when(subjectService.getActiveSubject(1L))
                .thenReturn(dto);

        SubjectDTO result = component.getSubjectById(1L);

        Assertions.assertEquals(dto, result);
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
    void shouldUpdateSubject() {
        UpdateSubjectDTO updateDto = new UpdateSubjectDTO();
        SubjectDTO expected = new SubjectDTO();

        when(subjectService.updateSubjectWithTopics(1L, updateDto))
                .thenReturn(expected);

        SubjectDTO result =
                component.updateSubjectWithTopics(1L, updateDto);

        Assertions.assertEquals(expected, result);
        verify(subjectService).updateSubjectWithTopics(1L, updateDto);
    }

    // ================================
    // deleteSubject
    // ================================

    @Test
    void shouldDeleteSubject() {
        component.deleteSubject(1L);

        verify(subjectService).deleteSubject(1L);
    }

    @Test
    void shouldThrowBadRequestWhenDeleteFails() {
        doThrow(new RuntimeException())
                .when(subjectService)
                .deleteSubject(1L);

        ResponseStatusException ex =
                Assertions.assertThrows(ResponseStatusException.class,
                        () -> component.deleteSubject(1L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals(
                "Erro ao deletar dados de disciplinas.",
                ex.getReason()
        );
    }
}
