package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectTopicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyCycleControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/mock-api/study-cycle";


    @Test
    public void shouldReturnOk_whenValidRequest() throws Exception {
        SubjectTopicDTO topic1 = new SubjectTopicDTO();
        topic1.idSubject = 3L;
        topic1.idTopic = 1L;

        SubjectTopicDTO topic2 = new SubjectTopicDTO();
        topic2.idSubject = 4L;
        topic2.idTopic = 2L;

        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                "Ciclo de Estatística",
                "Revisar probabilidade e regressão linear",
                Arrays.asList(topic1, topic2)
        );
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequest_whenDescriptionIsNull() throws Exception {
        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                null, // inválido
                "Alguma anotação",
                List.of(new SubjectTopicDTO())
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequest_whenTopicsIsNull() throws Exception {
        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                "Descrição válida",
                "Annotation válida",
                null // inválido
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequest_whenTopicsIsEmpty() throws Exception {
        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                "Descrição válida",
                "Annotation válida",
                Collections.emptyList() // vazio
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequest_whenDescriptionTooLong() throws Exception {
        SubjectTopicDTO topic = new SubjectTopicDTO();
        topic.idSubject = 1L;
        topic.idTopic = 2L;

        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                "D".repeat(101), // >100 caracteres
                "Annotation válida",
                List.of(topic)
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequest_whenAnnotationTooLong() throws Exception {
        SubjectTopicDTO topic = new SubjectTopicDTO();
        topic.idSubject = 1L;
        topic.idTopic = 1L;

        InsertStudyCycleDTO dto = new InsertStudyCycleDTO(
                "Descrição válida",
                "A".repeat(251), // >250 caracteres
                List.of(topic)
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
