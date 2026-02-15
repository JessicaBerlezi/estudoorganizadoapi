package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.LocalDateTime;
import java.util.*;

public class TopicMapper {

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_, _, _ -> new")
    public static TopicEntity convertToEntity(InsertTopicDTO dto, SubjectEntity subject, Integer order) {
        if (dto.description == null || dto.description.isEmpty()) {
            throw ApiExceptionFactory.badRequest(BusinessError.TOPIC_DESCRIPTION);
        }
        return new TopicEntity(
                dto.description,
                order,
                dto.incidenceScore,
                dto.knowledgeScore,
                dto.reviewIntervals,
                dto.annotation,
                subject
        );
    }

    public static TopicSummaryDTO convertToSummaryDTO(TopicEntity entity) {
        return fillSummaryFields(new TopicSummaryDTO(), entity);
    }

    private static <T extends TopicSummaryDTO> T fillSummaryFields(T dto, TopicEntity entity) {
        dto.id = entity.getId();
        dto.order = entity.getOrder();
        dto.description = entity.getDescription();
        dto.color = Utils.resolveTopicColor(entity.getIncidenceScore(), entity.getKnowledgeScore());
        dto.elapsedTime = "0min";
        dto.score = "-%";
        return dto;
    }

    public static TopicEntity updateEntity(TopicEntity entity, UpdateTopicDTO dto, int order){
        entity.setOrder(order);
        entity.setDescription(dto.description);
        entity.setAnnotation(dto.annotation);
        entity.setIncidenceScore(dto.incidenceScore);
        entity.setKnowledgeScore(dto.knowledgeScore);
        entity.setUpdatedAt(LocalDateTime.now());

        return entity;
    }
}
