package br.pucrs.estudoorganizado.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import br.pucrs.estudoorganizado.controller.dto.UpdateOrderDTO;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudyMapServiceOrderTest {

    private final StudyMapService service = new StudyMapService();

    @Test
    void shouldThrowExceptionWhenMaxOrderIsGreaterThanListSize() {

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 5) // inválido
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.updateSubjectsOrder(list));
    }

    @Test
    void shouldNotThrowWhenOrdersAreValid() {

        List<UpdateOrderDTO> list = List.of(
                subject(1L, 1),
                subject(2L, 2),
                subject(3L, 3)
        );

        assertDoesNotThrow(() -> service.updateSubjectsOrder(list));
    }

    private UpdateOrderDTO subject(Long id, int order) {
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.id = id;
        dto.order = order;
        return dto;
    }
}
