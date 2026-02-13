package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.service.StudyMapService;
import br.pucrs.estudoorganizado.service.SubjectService;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudyMapComponent {

    private final StudyMapService service;
    private final SubjectService subjectService;

    private static final Logger logger = LoggerFactory.getLogger(StudyMapComponent.class);

    public StudyMapDTO getStudyMap() {
        try {
            return service.getStudyMaps();
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_MAPS_LOAD);
        }
    }

    public SubjectDTO createSubjectWithTopics(InsertSubjectDTO dto) {
        try {
            return subjectService.createSubjectWithTopics(dto);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_MAP_CREATE);
        }
    }

    public SubjectDTO getSubjectById(Long subjectId) {
        try {
             SubjectEntity entity = subjectService.getActiveSubject(subjectId);
            return SubjectMapper.convertToDTO(entity);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_MAP_LOAD);
        }
    }

    public SubjectDTO updateSubjectWithTopics(Long subjectId, UpdateSubjectDTO dto) {
        try {
            SubjectEntity subject = subjectService.getActiveSubject(subjectId);
            logger.info("Subject to be updated: {}, new info: {}", subject, dto);

            SubjectEntity updatedSubject = SubjectMapper.updateEntity(subject, dto);

            Map<Long, TopicEntity> existingTopics = subject.getTopics().stream()
                    .filter(t -> t.getId() != null)
                    .collect(Collectors.toMap(TopicEntity::getId, Function.identity()));

            List<TopicEntity> topicsToRemove = topicsToRemove(existingTopics, dto.topics);
            if (!topicsToRemove.isEmpty()) {
                updatedSubject.getTopics().removeAll(topicsToRemove);
            }

            int order = 1;
            for (UpdateTopicDTO topicDTO : dto.topics) {
                if (topicDTO.id != null && existingTopics.containsKey(topicDTO.id)) {
                    TopicEntity existingTopic = existingTopics.get(topicDTO.id);
                    TopicEntity updatedTopic = TopicMapper.updateEntity(existingTopic, topicDTO, order++);
                   updatedSubject.getTopics().remove(existingTopic);
                    updatedSubject.getTopics().add(updatedTopic);
                } else {
                    TopicEntity newTopic = TopicMapper.convertToEntity(topicDTO, subject, order++);
                    updatedSubject.getTopics().add(newTopic);
                }
            }
            SubjectEntity entity = subjectService.updateSubjectWithTopics(updatedSubject);
            logger.info("Subject updated: {}", entity);
            return SubjectMapper.convertToDTO(entity);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_MAP_UPDATE);
        }
    }

    private List<TopicEntity> topicsToRemove(
            Map<Long, TopicEntity> existingTopics,
            List<UpdateTopicDTO> incomingTopics) {

        if (incomingTopics == null || incomingTopics.isEmpty()) {
            // Se o usuário enviou lista vazia, todos os existentes viram órfãos
            return new ArrayList<>(existingTopics.values());
        }
        Set<Long> incomingIds = incomingTopics.stream()
                .map(UpdateTopicDTO::getId)
                .filter(Objects::nonNull) //Id pode vir null para os tópicos novos
                .collect(Collectors.toSet());

        // Tudo que existe na base mas não está no DTO = órfão
        return existingTopics.entrySet().stream()
                .filter(entry -> !incomingIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


    public void disableSubject(Long subjectId) {
        try {
            subjectService.disableSubject(subjectId);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.badRequest(BusinessError.SUBJECT_DISABLE);
        }
    }
}
