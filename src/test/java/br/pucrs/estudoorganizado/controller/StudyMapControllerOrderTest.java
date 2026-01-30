package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.UpdateOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyMapControllerOrderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/mock-api/study-map/subjects/order";


    @Test
    void shouldFailWhenSubjectsOrderListIsEmpty() throws Exception {

        UpdateSubjectOrderDTO dto = new UpdateSubjectOrderDTO();
        dto.subjectsOrder = List.of();

        mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenSubjectIdIsNull() throws Exception {

        UpdateSubjectOrderDTO dto = new UpdateSubjectOrderDTO();
        dto.subjectsOrder = List.of(
                subjectOrder(null, 1)
        );

        mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenOrderIsLessThanOne() throws Exception {

        UpdateSubjectOrderDTO dto = new UpdateSubjectOrderDTO();
        dto.subjectsOrder = List.of(
                subjectOrder(1L, 0)
        );

        mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    private UpdateOrderDTO subjectOrder(Long id, int order) {
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.id = id;
        dto.order = order;
        return dto;
    }


}
