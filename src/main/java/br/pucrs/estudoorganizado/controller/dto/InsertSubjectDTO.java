package br.pucrs.estudoorganizado.controller.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InsertSubjectDTO {
    @NotBlank(message = "Nome da disciplina é obrigatória")
    @Size(max = 100, message = "Nome da disciplina deve ter no máximo 100 caracteres")
    public String description;

    @Size(max = 250, message = "Anotação deve ter no máximo 250 caracteres")
    public String annotation;

    @Valid
    public List<InsertTopicDTO> topics;

    public String toLogString() {
        String topicsLog = (topics == null || topics.isEmpty())
                ? "[]"
                : topics.stream()
                .map(InsertTopicDTO::toLogString)
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format(
                "description='%s', annotation=%s, topics=%s",
                description,
                annotation != null && !annotation.isBlank(),
                topicsLog
        );
    }
}
