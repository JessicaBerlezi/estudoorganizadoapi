package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.Mocks;
import br.pucrs.estudoorganizado.component.StudyMapComponent;
import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class StudyMapControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudyMapComponent component;


    private static final String URL = "/v1/study-map";


    // ==========================
    // GET /v1/study-map
    // ==========================

    @Test
    void shouldReturnOkWhenGetStudyMap() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    // ==========================
    // POST /v1/study-map/subject
    // ==========================

    @Test
    void shouldReturnOkWhenValidInsertSubject() throws Exception {

        InsertTopicDTO topic = new InsertTopicDTO();
        topic.description = "Tópico 1";
        topic.reviewIntervals = List.of(1, 3, 5);

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Matemática";
        dto.annotation = "Disciplina base";
        dto.topics = List.of(topic);

        when(component.createSubjectWithTopics(any()))
                .thenReturn(1L);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(component).createSubjectWithTopics(any());
    }


    @Test
    void shouldReturnBadRequestWhenDescriptionIsNull() throws Exception {

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = null; // inválido
        dto.annotation = "Annotation válida";
        dto.topics = List.of(new InsertTopicDTO());

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionTooLong() throws Exception {

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "D".repeat(101);
        dto.annotation = "Annotation válida";
        dto.topics = List.of(new InsertTopicDTO());

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAnnotationTooLong() throws Exception {

        InsertSubjectDTO dto = new InsertSubjectDTO();
        dto.description = "Descrição válida";
        dto.annotation = "A".repeat(251);
        dto.topics = List.of(new InsertTopicDTO());

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ==========================
    // GET /v1/study-map/subject
    // ==========================

    @Test
    void shouldReturnOkWhenGetSubjectById() throws Exception {

        when(component.getSubjectById(1L))
                .thenReturn(new StudyStructureDTO());

        mockMvc.perform(get(URL + "/subject")
                        .param("subjectId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenSubjectIdIsMissing() throws Exception {

        mockMvc.perform(get(URL + "/subject"))
                .andExpect(status().isBadRequest());
    }

    // ==========================
    // PUT /v1/study-map/subject
    // ==========================

    @Test
    void shouldReturnOkWhenValidUpdate() throws Exception {

        SubjectEntity subject = Mocks.createSubjectEntityMock();
        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.description = "Nova descrição";
        dto.annotation = "Nova annotation";
        dto.topics = List.of();

        when(component.updateSubjectWithTopics(eq(1L), any(UpdateSubjectDTO.class)))
                .thenReturn(1L);

        mockMvc.perform(put(URL + "/subject")
                        .param("subjectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(component).updateSubjectWithTopics(eq(1L), any(UpdateSubjectDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateDescriptionIsNull() throws Exception {

        UpdateSubjectDTO dto = new UpdateSubjectDTO();
        dto.description = null; // inválido
        dto.annotation = "Annotation";

        mockMvc.perform(put(URL + "/subject")
                        .param("subjectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ==========================
    // DELETE /v1/study-map/subject
    // ==========================

    @Test
    void shouldReturnOkWhenDisableSubject() throws Exception {

        doNothing().when(component).disableSubject(1L);

        mockMvc.perform(delete(URL + "/subject")
                        .param("subjectId", "1"))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnBadRequestWhenDisableSubjectIdMissing() throws Exception {

        mockMvc.perform(delete(URL + "/subject"))
                .andExpect(status().isBadRequest());
    }
}
