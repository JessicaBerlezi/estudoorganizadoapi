package br.pucrs.estudoorganizado.controller.dto;


public class InsertTopicStructureDTO extends BaseTopicStructureDTO{

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
