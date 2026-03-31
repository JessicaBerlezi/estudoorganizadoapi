package br.pucrs.estudoorganizado.controller;


import br.pucrs.estudoorganizado.component.StudyCycleComponent;
import br.pucrs.estudoorganizado.controller.dto.UpdateStudyCycleStructureDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.service.TopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyCycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudyCycleComponent component;


    @MockitoBean
    private TopicService topicService;

    private static final String URL = "/v1/study-cycle";

    @Test
    void shouldReturnOkWhenGetStudyCycle() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateStudyCycleSuccessfully() throws Exception {

        StudyCycleEntity entity = new StudyCycleEntity();
        entity.setId(1L);

        Mockito.when(component.creteStudyCycle(Mockito.any())).thenReturn(entity);

        UpdateStudyCycleStructureDTO dto = new UpdateStudyCycleStructureDTO(
                "Ciclo de Estudos",
                "Anotação válida",
                List.of(1L, 2L)
        );
        mockMvc.perform(post(URL + "/cycle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateStudyCycleSuccessfully() throws Exception {

        Mockito.doNothing().when(component).updateStudyCycle(Mockito.eq(1L), Mockito.any());

        UpdateStudyCycleStructureDTO dto = new UpdateStudyCycleStructureDTO(
                "Ciclo Atualizado",
                "Anotação válida",
                List.of(1L, 2L)
        );

        mockMvc.perform(put(URL + "/cycle")
                        .param("cycleId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteStudyCycleSuccessfully() throws Exception {
        Mockito.doNothing().when(component).disableSubject(1L);

        mockMvc.perform(delete(URL + "/cycle")
                        .param("cycleId", "1"))
                .andExpect(status().isOk());
    }

    // ---------- FALHAS DE VALIDAÇÃO ----------

    @Test
    void shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        UpdateStudyCycleStructureDTO dto = new UpdateStudyCycleStructureDTO(
                "   ", // inválido
                "Anotação válida",
                List.of(1L)
        );

        mockMvc.perform(post(URL + "/cycle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(BusinessError.CYCLE_DESCRIPTION.message()));
    }

    @Test
    void shouldReturnBadRequestWhenTopicsAreEmpty() throws Exception {
        UpdateStudyCycleStructureDTO dto = new UpdateStudyCycleStructureDTO(
                "Ciclo válido",
                "Anotação válida",
                List.of() // inválido
        );

        mockMvc.perform(post(URL + "/cycle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(BusinessError.TOPIC_MANDATORY.message()));
    }
}
