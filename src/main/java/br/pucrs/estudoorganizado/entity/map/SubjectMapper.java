package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectMapper {

    public static SubjectEntity convertToEntity(InsertSubjectDTO dto){
        SubjectEntity subject = new SubjectEntity(
                dto.getDescription(),
                dto.getAnnotation()
        );

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
                    topicDTO.rgb = topic.getColor();
                    topicDTO.description = topic.getDescription();
                    return topicDTO;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        return dto;
    }
}
