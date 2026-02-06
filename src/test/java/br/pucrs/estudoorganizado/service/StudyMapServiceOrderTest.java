package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicOrderDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import br.pucrs.estudoorganizado.controller.dto.UpdateOrderDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)

public class StudyMapServiceOrderTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private StudyMapService service;

    @Test
    void shouldThrowExceptionWhenSubjectMaxOrderIsGreaterThanListSize() {

        UpdateSubjectOrderDTO requestDTO = new UpdateSubjectOrderDTO();

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 5) // inválido
        );
        requestDTO.subjectsOrder = list;

        assertThrows(IllegalArgumentException.class,
                () -> service.updateSubjectsOrder(requestDTO));
    }


    @Test
    void shouldThrowExceptionWhenTopicsMaxOrderIsGreaterThanListSize() {

        UpdateTopicOrderDTO requestDTO = new UpdateTopicOrderDTO();

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 5) // inválido
        );
        requestDTO.topicsOrder = list;

        assertThrows(IllegalArgumentException.class,
                () -> service.updateTopicsOrder(requestDTO));
    }

    @Test
    void shouldNotThrowWhenSubjectOrdersAreValid() {
        UpdateSubjectOrderDTO requestDTO = new UpdateSubjectOrderDTO();

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 3)
        );
        requestDTO.subjectsOrder = list;

        assertDoesNotThrow(() -> service.updateSubjectsOrder(requestDTO));
    }


    @Test
    void shouldNotThrowWhenTopicsOrdersAreValids() {
        UpdateTopicOrderDTO requestDTO = new UpdateTopicOrderDTO();

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 3)
        );
        requestDTO.subjectId = 1L;
        requestDTO.topicsOrder = list;

        assertDoesNotThrow(() -> service.updateTopicsOrder(requestDTO));
    }

    private UpdateOrderDTO subject(Long id, int order) {
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.id = id;
        dto.order = order;
        return dto;
    }
}
