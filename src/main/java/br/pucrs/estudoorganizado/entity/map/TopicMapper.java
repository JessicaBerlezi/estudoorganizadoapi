package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.InsertTopicDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicDetailDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicDTO;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;

public class TopicMapper {

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_, _, _ -> new")
    public static TopicEntity convertToEntity(InsertTopicDTO dto, SubjectEntity subject, Integer order) {
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

    public static TopicSummaryDTO convertToSummaryDTO(TopicEntity entity) {

        TopicSummaryDTO dto = new TopicSummaryDTO();
        dto.id = entity.getId();
        dto.order = entity.getOrder();
        dto.description = entity.getDescription();
        dto.rgb = entity.getColor() != null ? entity.getColor() : "#9E9E9E";
        dto.elapsedTime = "0min";
        dto.score = "-%";

        return dto;
    }


    public static TopicDetailDTO convertToDetailDTO(TopicEntity entity) {

        TopicDetailDTO dto = new TopicDetailDTO();
        convertToSummaryDTO(entity);
        dto.annotation = entity.getAnnotation();
        dto.subject = entity.getSubject().getDescription();

        return dto;
    }

    public static TopicEntity update(TopicEntity entity, UpdateTopicDTO dto, Integer order) {
        TopicEntity topic = entity;
        topic.setDescription(dto.description);
        topic.setOrder(order);
        topic.setIncidenceScore(dto.incidenceScore);
        topic.setKnowledgeScore(dto.knowledgeScore);
        topic.setReviewIntervalsDays(dto.reviewIntervals);
        topic.setAnnotation(dto.annotation);
        topic.setColor("#9E9E9E");

        return topic;
    }
}
