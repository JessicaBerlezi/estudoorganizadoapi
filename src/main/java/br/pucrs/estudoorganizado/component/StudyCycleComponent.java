package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.controller.dto.DailyTasksDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleDetailsDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleWithTopicsDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.entity.map.StudyCycleMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.service.ReviewControlService;
import br.pucrs.estudoorganizado.service.StudyCycleItemService;
import br.pucrs.estudoorganizado.service.StudyCycleService;
import br.pucrs.estudoorganizado.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudyCycleComponent {

    private final StudyCycleService service;
    private final TopicService topicService;
    private final StudyCycleItemService itemService;
    private final ReviewControlService reviewControlService;

    private static final Logger logger = LoggerFactory.getLogger(StudyCycleComponent.class);

    public DailyTasksDTO getDailyTasks() {
        try {
            DailyTasksDTO response = new DailyTasksDTO();
            response.reviews = reviewControlService.getReviewAgenda();
            response.cycles = service.findActiveCyclesWithFullHistory();
            return response;
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_CYCLES_LOAD);
        }
    }

    public StudyCycleWithTopicsDTO getStudyCycleById(Long cycleId) {
        try {
            return service.getCycleWithFullHistoryById(cycleId);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_CYCLE_LOAD);
        }
    }

    public StudyCycleEntity creteStudyCycle(StudyCycleDetailsDTO dto) {
        try {
            List<TopicEntity> foundTopics = topicService.getExistingTopicsById(dto.getTopics());

            if (foundTopics == null || foundTopics.isEmpty()) {
                logger.warn("Tentativa de criar ciclo sem tópicos válidos. Parâmetros=" + dto.toLogString());
                throw ApiExceptionFactory.badRequest(BusinessError.TOPIC_NOT_FOUND);
            }

            StudyCycleEntity cycle = StudyCycleMapper.fromDTO(dto);

            List<StudyCycleItemEntity> items = foundTopics.stream()
                    .map(topic -> {
                        StudyCycleItemEntity item = new StudyCycleItemEntity();
                        item.setStudyCycle(cycle);
                        item.setTopic(topic);
                        return item;
                    })
                    .toList();
            cycle.setItems(items);

            return service.saveStudyCycle(cycle);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_CYCLE_CREATE);
        }
    }

    public void updateStudyCycle(Long cycleId, StudyCycleDetailsDTO dto) {
        try {
            StudyCycleEntity cycle = service.getStudyCycle(cycleId);

            Set<Long> requestedTopicIds = new HashSet<>(dto.getTopics());

            List<StudyCycleItemEntity> existingItems = itemService.findAllByStudyCycleId(cycleId);

            List<StudyCycleItemEntity> itemsToRemove = itemsToRemove(existingItems, requestedTopicIds);
            if (!itemsToRemove.isEmpty()) {
                cycle.getItems().removeAll(itemsToRemove);
            }

            List<Long> topicIdsToAdd = topicIdsToAdd(existingItems, requestedTopicIds);

            if (!topicIdsToAdd.isEmpty()) {
                List<TopicEntity> topicsToAdd = topicService.getExistingTopicsById(
                        topicIdsToAdd
                );

                List<StudyCycleItemEntity> itemsToAdd = topicsToAdd.stream()
                        .map(topic -> {
                            StudyCycleItemEntity item = new StudyCycleItemEntity();
                            item.setStudyCycle(cycle);
                            item.setTopic(topic);
                            return item;
                        })
                        .toList();
                cycle.getItems().addAll(itemsToAdd);
            }

            if (!Objects.equals(cycle.getDescription(), dto.getDescription())) {
                cycle.setDescription(dto.getDescription());
            }

            if (!Objects.equals(cycle.getAnnotation(), dto.getAnnotation())) {
                cycle.setAnnotation(dto.getAnnotation());
            }
            service.saveStudyCycle(cycle);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_CYCLE_UPDATE);
        }
    }

    private List<StudyCycleItemEntity> itemsToRemove(List<StudyCycleItemEntity> existingItems, Set<Long> requestedTopicIds) {
        return existingItems.stream()
                .filter(item -> !requestedTopicIds.contains(item.getTopic().getId()))
                .toList();
    }

    private List<Long> topicIdsToAdd(List<StudyCycleItemEntity> existingItems, Set<Long> requestedTopicIds) {
        Set<Long> existingTopicIds = existingItems.stream()
                .map(item -> item.getTopic().getId())
                .collect(Collectors.toSet());

        return requestedTopicIds.stream()
                .filter(id -> !existingTopicIds.contains(id)).distinct().collect(Collectors.toList());
    }

    public void disableSubject(Long cycleId) {
        try {
            StudyCycleEntity cycle = service.getStudyCycle(cycleId);
            service.deleteStudyCycle(cycle);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw ApiExceptionFactory.internalError(BusinessError.STUDY_CYCLE_DELETE);
        }
    }
}
