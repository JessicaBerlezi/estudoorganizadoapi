package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.LocalDateTime;

public class TopicMapper {

    public static TopicEntity createTopicAndConvertToEntity(BaseTopicStructureDTO dto, SubjectEntity subject, Integer order) {
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
    public static TopicEntity updateExistingTopicAndConvertToEntity(UpdateTopicStructureDTO dto, TopicEntity existing, int order){
        if (dto.id == null || !dto.id.equals(existing.getId())) {
            throw ApiExceptionFactory.badRequest(BusinessError.TOPIC_ID_REQUIRED);
        }
        existing.setDescription(dto.description);
        existing.setIncidenceScore(dto.incidenceScore);
        existing.setKnowledgeScore(dto.knowledgeScore);
        existing.setReviewIntervalsDays(dto.reviewIntervals);
        existing.setAnnotation(dto.annotation);
        existing.setOrder(order);
        existing.setUpdatedAt(LocalDateTime.now());

        return existing;
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
}
