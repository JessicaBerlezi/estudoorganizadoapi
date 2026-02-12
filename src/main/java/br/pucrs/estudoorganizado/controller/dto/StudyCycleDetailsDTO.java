package br.pucrs.estudoorganizado.controller.dto;

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
public class StudyCycleDetailsDTO {
    @NotBlank(message = "Nome do ciclo de estudo é obrigatório")
    @Size(max = 100, message = "Nome do ciclo de estudo deve ter no máximo 100 caracteres")
    String description;

    @Size(max = 250, message = "Anotação deve ter no máximo 250 caracteres")
    String annotation;

    @Valid
    @NotEmpty(message = "Algum tópico deve ser selecionada")
    List<Long> topics;

    @JsonCreator
    public StudyCycleDetailsDTO(
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
