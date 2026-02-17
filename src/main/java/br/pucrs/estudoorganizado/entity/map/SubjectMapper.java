package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;

import java.time.LocalDateTime;
import java.util.*;

public class SubjectMapper {

    public static SubjectEntity createSubjectAndConvertToEntity(InsertSubjectStructureDTO dto) {

        SubjectEntity subject = new SubjectEntity(
                dto.getDescription(),
                dto.getAnnotation(),
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> entities = new ArrayList<>();
        int order = 1;
        for (InsertTopicStructureDTO topicDTO : dto.getTopics()) {
            entities.add(TopicMapper.createTopicAndConvertToEntity(
                    topicDTO,
                    subject,
                    order
            ));
            order++;
        }
        subject.setTopics(entities);

        return subject;
    }

    public static SubjectEntity updateExistingSubjectAndConvertToEntity(SubjectEntity entity, UpdateSubjectStructureDTO dto) {

        entity.setDescription(dto.getDescription());
        entity.setAnnotation(dto.annotation);
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
