package br.pucrs.estudoorganizado.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateTopicOrderDTO {

    @NotNull(message = "Id da disciplina é obrigatório")
    public Long subjectId;

    @NotEmpty(message = "Necessária lista para atualização da ordem")
    @Valid
    public List<UpdateOrderDTO> subjectsOrder;


    public String toLogString() {
        String topicsLog = (subjectsOrder == null || subjectsOrder.isEmpty())
                ? "[]"
                : subjectsOrder.stream()
                .map(UpdateOrderDTO::toString)
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format(
                "subjectId='%s', order='%s'",
                subjectId,
                topicsLog
        );
    }
}
