package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.Mocks;
import br.pucrs.estudoorganizado.component.StudyMapComponent;
import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.eq;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.reviewIntervals = List.of(1, 3, 5);

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
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
    void shouldCreateSubjectSuccessfully() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Direito Constitucional";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        StudyStructureDTO response = new StudyStructureDTO();

        Mockito.when(component.createSubjectWithTopics(Mockito.any())).thenReturn(1L);
        Mockito.when(component.getSubjectById(1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/study-map/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowsDescriptionBlankedInCreateSubject() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "  ";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        StudyStructureDTO response = new StudyStructureDTO();

        Mockito.when(component.createSubjectWithTopics(Mockito.any())).thenReturn(1L);
        Mockito.when(component.getSubjectById(1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/study-map/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.SUBJECT_NAME_REQUIRED));
    }

    @Test
    void shouldThrowsDescriptionNullInCreateSubject() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        StudyStructureDTO response = new StudyStructureDTO();

        Mockito.when(component.createSubjectWithTopics(Mockito.any())).thenReturn(1L);
        Mockito.when(component.getSubjectById(1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/study-map/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.SUBJECT_NAME_REQUIRED));
    }



    @Test
    void shouldReturnBadRequestWhenDescriptionTooLong() throws Exception {
        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "D".repeat(151);
        dto.annotation = "Annotation válida";
        dto.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.SUBJECT_NAME_MAX));
    }


    @Test
    void shouldReturnBadRequestWhenAnnotationTooLong() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO dto = new InsertSubjectStructureDTO();
        dto.description = "Disciplina base";
        dto.annotation =  "D".repeat(301);
        dto.topics = List.of(topic);


        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.ANNOTATION_MAX));
    }

    @Test
    void shouldReturnBadRequestWhenIncidenceScoreHasHigherWrongValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 6;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.INCIDENCE_RANGE));
    }


    @Test
    void shouldReturnBadRequestWhenIncidenceScoreHasLowerWrongValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = -5;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.INCIDENCE_RANGE));
    }


    @Test
    void shouldReturnBadRequestWhenKnowledgeScoreHasHigherWrongValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 0;
        topic.knowledgeScore = 3;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.KNOWLEDGE_RANGE));
    }


    @Test
    void shouldReturnBadRequestWhenKnowledgeScoreHasLowerWrongValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = -1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.KNOWLEDGE_RANGE));
    }


    @Test
    void shouldReturnBadRequestWhenReviewIntervalsScoreHasEmptyValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 0;
        topic.reviewIntervals = List.of();
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.REVIEW_INTERVALS_EMPTY));
    }



    @Test
    void shouldReturnBadRequestWhenReviewIntervalsScoreHasLowerHigherValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30, 120);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.REVIEW_INTERVALS_INVALID));
    }

    @Test
    void shouldReturnBadRequestWhenReviewIntervalsScoreHasLowerWrongValue() throws Exception {

        InsertTopicStructureDTO topic = new InsertTopicStructureDTO();
        topic.description = "Tópico 1";
        topic.incidenceScore =3;
        topic.knowledgeScore = 0;
        topic.reviewIntervals = List.of(1, -17, 30);
        topic.annotation = "Anotação";

        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = List.of(topic);

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.REVIEW_INTERVALS_INVALID));
    }

    @Test
    void shouldReturnBadRequestWhenTopicListIsEmpty() throws Exception {
        InsertSubjectStructureDTO request = new InsertSubjectStructureDTO();
        request.description = "Disciplina base";
        request.annotation = "Disciplina base";
        request.topics = Collections.emptyList();

        mockMvc.perform(post(URL + "/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(ValidationMessages.TOPIC_REQUIRED));
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

        UpdateTopicStructureDTO topic = new UpdateTopicStructureDTO();
        topic.id = 10L; // obrigatório na atualização
        topic.description = "Tópico atualizado";
        topic.incidenceScore = 2;
        topic.knowledgeScore = 1;
        topic.reviewIntervals = List.of(1, 7, 30);
        topic.annotation = "Anotação atualizada";

        UpdateSubjectStructureDTO request = new UpdateSubjectStructureDTO();
        request.description = "Direito Administrativo";
        request.annotation = "Disciplina ajustada";
        request.topics = List.of(topic);

        when(component.updateSubjectWithTopics(eq(1L), any(UpdateSubjectStructureDTO.class)))
                .thenReturn(1L);

        mockMvc.perform(put(URL + "/subject")
                        .param("subjectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(component).updateSubjectWithTopics(eq(1L), any(UpdateSubjectStructureDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateDescriptionIsNull() throws Exception {

        UpdateSubjectStructureDTO dto = new UpdateSubjectStructureDTO();
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
