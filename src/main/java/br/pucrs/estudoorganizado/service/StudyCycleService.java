package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.TopicStudyHistoryView;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.StudyCycleMapper;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;
import br.pucrs.estudoorganizado.repository.TopicStudyHistoryViewRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCycleService {

    private final TopicStudyHistoryViewRepository viewRepository;
    private final StudyCycleRepository studyCycleRepository;
    private final TopicRepository topicRepository;

    public void create(InsertStudyCycleDTO dto) {

        List<StudyCycleItemEntity> saveAt = new ArrayList<>();
        StudyCycleEntity cycle = StudyCycleEntity.fromDTO(dto);

        for (SubjectTopicDTO itemDTO : dto.getTopics()) {
            TopicEntity topic = topicRepository.findById(itemDTO.getIdTopic()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found: " + itemDTO.getIdTopic()));
            saveAt.add(new StudyCycleItemEntity(cycle, topic));
        }
        cycle.setItems(saveAt);
        studyCycleRepository.save(cycle);
    }

    public StudyCycleDTO getActiveStudyCycles() {
        StudyCycleDTO response = new StudyCycleDTO();
        response.reviews = null;
        response.cycles = findActiveCyclesWithFullHistory();;
        return response;

    }


    private List<StudyCycleWithTopicsDTO> findActiveCyclesWithFullHistory() {
        List<TopicStudyHistoryView> rows =
                viewRepository.findAllByOrderByCycleStartedAtAscCycleIdAscTopicOrderAscRecordStartedAtDesc();

        Map<Long, StudyCycleWithTopicsDTO> cycleMap = new LinkedHashMap<>();
        Map<Long, TopicWithHistoryDTO> topicMap = new HashMap<>();
        Map<Long, Set<Integer>> reviewDaysMap = new HashMap<>();

        for (TopicStudyHistoryView row : rows) {

            StudyCycleWithTopicsDTO cycle = cycleMap.computeIfAbsent(
                    row.getCycleId(),
                    id -> StudyCycleMapper.toCycleDTO(row)
            );

            TopicWithHistoryDTO topic = topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicWithHistoryDTO t = TopicMapper.toTopicDTO(row);
                        cycle.getTopics().add(t);
                        return t;
                    }
            );

            if (row.getRecordId() != null) {
                topic.getHistory().add(
                        TopicMapper.toHistoryDTO(row)
                );
            }
        }
        return new ArrayList<>(cycleMap.values());
    }
}
