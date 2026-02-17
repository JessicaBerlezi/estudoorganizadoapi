package br.pucrs.estudoorganizado.controller.dto;


import lombok.Getter;

@Getter
public class UpdateTopicStructureDTO extends BaseTopicStructureDTO {

    public Long id;

    public String toLogString() {
        return String.format(
                "Id=='%s', topic='%s', incidence=%d, knowledge=%d, intervals=%s",
                id,
                description,
                incidenceScore,
                knowledgeScore,
                reviewIntervals
        );
    }
}
