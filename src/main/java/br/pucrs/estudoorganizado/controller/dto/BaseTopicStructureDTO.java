package br.pucrs.estudoorganizado.controller.dto;

import br.pucrs.estudoorganizado.controller.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class BaseTopicStructureDTO {
    @NotBlank(message = ValidationMessages.TOPIC_NAME_REQUIRED)
    @Size(max = 150, message = ValidationMessages.TOPIC_NAME_MAX)
    public String description;

    @Min(value = 0, message = ValidationMessages.INCIDENCE_RANGE)
    @Max(value = 4, message = ValidationMessages.INCIDENCE_RANGE)
    public int incidenceScore;

    @Min(value = 0, message = ValidationMessages.KNOWLEDGE_RANGE)
    @Max(value = 2, message = ValidationMessages.KNOWLEDGE_RANGE)
    public int knowledgeScore;

    @Valid
    @NotEmpty(message = ValidationMessages.REVIEW_INTERVALS_EMPTY)
    public List<@Min(value = 1, message = ValidationMessages.REVIEW_INTERVALS_INVALID) @Max(value = 90, message = ValidationMessages.REVIEW_INTERVALS_INVALID) Integer> reviewIntervals;

    @Size(max = 250, message = ValidationMessages.ANNOTATION_MAX)
    public String annotation;
}
