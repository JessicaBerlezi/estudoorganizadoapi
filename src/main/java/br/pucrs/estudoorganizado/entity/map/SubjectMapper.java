package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.service.utils.Utils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SubjectMapper {

    public static SubjectEntity convertToEntity(InsertSubjectDTO dto){
        SubjectEntity subject = new SubjectEntity(
                dto.getDescription(),
                dto.getAnnotation(),
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> entities = new ArrayList<>();
        int order = 1;
        for (InsertTopicDTO topicDTO : dto.getTopics()) {
            entities.add(TopicMapper.convertToEntity(
                    topicDTO,
                    subject,
                    order
            ));
            order++;
        }
        subject.setTopics(entities);
        return subject;
    }

    public static SubjectDTO convertToDTO(SubjectEntity entity) {
        SubjectDTO dto = new SubjectDTO();
        dto.id = entity.getId();
        dto.description = entity.getDescription();

        dto.topics = entity.getTopics().stream()
                .filter(topic -> Boolean.TRUE.equals(topic.getIsActive()))
                .sorted(Comparator.comparing(TopicEntity::getOrder, Comparator.nullsLast(Integer::compareTo)))
                .map(topic -> {
                    TopicSummaryDTO topicDTO = new TopicSummaryDTO();
                    topicDTO.id = topic.getId();
                    topicDTO.order = topic.getOrder();
                    topicDTO.color = Utils.resolveTopicColor(topic.getIncidenceScore(), topic.getKnowledgeScore());
                    topicDTO.description = topic.getDescription();
                    return topicDTO;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        return dto;
    }

    public static SubjectEntity updateEntity(SubjectEntity entity, UpdateSubjectDTO dto) {
        entity.setDescription(dto.getDescription());
        entity.setAnnotation(dto.annotation);
        entity.setUpdatedAt(LocalDateTime.now());
        return  entity;
    }
}
