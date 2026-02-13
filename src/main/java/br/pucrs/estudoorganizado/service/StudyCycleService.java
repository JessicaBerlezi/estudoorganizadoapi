package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.entity.view.CycleStudyView;
import br.pucrs.estudoorganizado.entity.map.StudyCycleMapper;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;
import br.pucrs.estudoorganizado.repository.CycleStudyViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCycleService {

    private final CycleStudyViewRepository viewRepository;
    private final StudyCycleRepository repository;

    public StudyCycleEntity saveStudyCycle(StudyCycleEntity cycle) {
        return repository.save(cycle);
    }

    public StudyCycleEntity getStudyCycle(Long cycleId) {
        return repository.findById(cycleId)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD));
    }

    public List<StudyCycleWithTopicsDTO> findActiveCyclesWithFullHistory() {
        List<CycleStudyView> rows =
                viewRepository.findAllByOrderByCycleStartedAtAscCycleIdAscTopicOrderAscRecordStartedAtDesc();

        Map<Long, StudyCycleWithTopicsDTO> cycleMap = new LinkedHashMap<>();
        Map<Long, TopicWithHistoryDTO> topicMap = new HashMap<>();

        for (CycleStudyView row : rows) {

            StudyCycleWithTopicsDTO cycle = cycleMap.computeIfAbsent(
                    row.getCycleId(),
                    id -> StudyCycleMapper.toCycleDTO(row)
            );

           topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicWithHistoryDTO t = TopicMapper.toTopicDTO(row);
                        cycle.getTopics().add(t);
                        return t;
                    }
            );
        }
        return new ArrayList<>(cycleMap.values());
    }

    public StudyCycleWithTopicsDTO getCycleWithFullHistoryById(Long cycleId) {

        List<CycleStudyView> rows = viewRepository.findByCycleId(cycleId);

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD);
        }

        StudyCycleWithTopicsDTO cycle = StudyCycleMapper.toCycleDTO(rows.getFirst());

        Map<Long, TopicWithHistoryDTO> topicMap = new HashMap<>();

        for (CycleStudyView row : rows) {

            topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicWithHistoryDTO topic =
                                TopicMapper.toTopicDTO(row);
                        cycle.getTopics().add(topic);
                        return topic;
                    }
            );
        }

        return cycle;
    }


    public void deleteStudyCycle(StudyCycleEntity cycle) {
        repository.delete(cycle);
    }
}
