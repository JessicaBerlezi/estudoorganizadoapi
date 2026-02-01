package br.pucrs.estudoorganizado.controller.dto;

import jakarta.validation.constraints.*;

public class InsertSubjectTopicDTO extends InsertTopicDTO{
    @NotNull(message = "Identificação da disciplina é obrigatória")
    public Long subjectId;

    public String toLogString() {
        return String.format(
                "subjectId='%s', topic='%s', incidence=%d, knowledge=%d, intervals=%s",
                subjectId,
                description,
                incidenceScore,
                knowledgeScore,
                reviewIntervals
        );
    }
}
