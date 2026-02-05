package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.InsertTopicDTO;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import jakarta.validation.constraints.NotNull;

public class TopicMapper {

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_, _, _ -> new")
    public static TopicEntity convertToEntity(@NotNull @org.jetbrains.annotations.NotNull InsertTopicDTO dto, @NotNull SubjectEntity subject, @NotNull Integer order) {
        return new TopicEntity(
                dto.description,
                order,
                dto.incidenceScore,
                dto.knowledgeScore,
                null, //dto
                dto.reviewIntervals,
                dto.annotation,
                subject
        );
    }
}
