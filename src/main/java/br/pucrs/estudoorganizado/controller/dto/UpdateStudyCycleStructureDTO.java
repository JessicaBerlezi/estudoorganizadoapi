package br.pucrs.estudoorganizado.controller.dto;

import br.pucrs.estudoorganizado.controller.ValidationMessages;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UpdateStudyCycleStructureDTO {

    @NotBlank(message = ValidationMessages.CYCLE_NAME_REQUIRED)
    @Size(max = 100, message = ValidationMessages.CYCLE_NAME_MAX)
    String description;

    @Size(max = 250, message = ValidationMessages.ANNOTATION_MAX)
    String annotation;

    @Valid
    @NotEmpty(message = ValidationMessages.TOPIC_REQUIRED)
    List<Long> topics;

    @JsonCreator
    public UpdateStudyCycleStructureDTO(
            @JsonProperty("description") String description,
            @JsonProperty("annotation") String annotation,
            @JsonProperty("topics") List<Long> topics) {
        this.description = description;
        this.annotation = annotation;
        this.topics = topics;
    }

    public String toLogString() {

        String topicsLog = (topics == null || topics.isEmpty())
                ? "[]"
                : topics.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]"));


        return String.format(
                "description='%s', annotation=%s, topicsId=%s",
                description,
                annotation != null && !annotation.isBlank(),
                topicsLog
        );
    }

}
