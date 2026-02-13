package br.pucrs.estudoorganizado.controller.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public class InsertTopicDTO {
    @NotBlank(message = "Nome do tópico é obrigatório")
    @Size(max = 150, message = "Nome do tópico deve ter no máximo 150 caracteres")
    public String description;

    @Min(value = 0, message = "Incidência mínima é 0")
    @Max(value = 4, message = "Incidência máxima é 4")
    public int incidenceScore;

    @Min(value = 0, message = "Domínio mínimo é 0")
    @Max(value = 2, message = "Domínio máximo é 2")
    public int knowledgeScore;

    @NotEmpty(message = "Lista de intervalos não pode ser vazia")
    public List<@Min(1) @Max(90) Integer> reviewIntervals;

    @Size(max = 250, message = "Anotação deve ter no máximo 250 caracteres")
    public String annotation;

    public String toLogString() {
        return String.format(
                "topic='%s', incidence=%d, knowledge=%d, intervals=%s",
                description,
                incidenceScore,
                knowledgeScore,
                reviewIntervals
        );
    }
}
