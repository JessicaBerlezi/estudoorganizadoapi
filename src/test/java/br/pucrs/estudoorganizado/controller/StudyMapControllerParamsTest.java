package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.InsertTopicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyMapControllerParamsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/mock-api/study-map/subject";


    @Test
    void shouldAllowSubjectCreationWithoutTopics() throws Exception {
        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Física";
        dto.annotation = null;
        dto.topics = null;

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    private InsertTopicDTO validTopic() {
        InsertTopicDTO topic = new InsertTopicDTO();
        topic.description = "Funções";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(7, 15, 30);
        topic.annotation = "Revisar antes da prova";
        return topic;
    }

    @Test
    void shouldReturnBadRequestWhenSubjectDescriptionIsBlank() throws Exception {
        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = ""; // inválido
        dto.annotation = "Alguma anotação";
        dto.topics = List.of(validTopic());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenSubjectDescriptionIsTooLong() throws Exception {
        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "A".repeat(101); // limite é 100
        dto.annotation = "ok";
        dto.topics = List.of(validTopic());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenTopicDescriptionIsBlank() throws Exception {
        InsertTopicDTO topic = validTopic();
        topic.description = ""; // inválido

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "ok";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenIncidenceScoreIsInvalid() throws Exception {
        InsertTopicDTO topic = validTopic();
        topic.incidenceScore = 5; // inválido

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "ok";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenKnowledgeScoreIsInvalid() throws Exception {
        InsertTopicDTO topic = validTopic();
        topic.knowledgeScore = 3; // inválido

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "ok";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenReviewIntervalsIsEmpty() throws Exception {
        InsertTopicDTO topic = validTopic();
        topic.reviewIntervals = List.of(); // inválido

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "ok";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenReviewIntervalIsOutOfRange() throws Exception {
        InsertTopicDTO topic = validTopic();
        topic.reviewIntervals = List.of(0, 10); // 0 é inválido

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "ok";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkWhenPayloadIsValid() throws Exception {
        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "Disciplina base";
        dto.topics = List.of(validTopic());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}
