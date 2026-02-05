package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.StudyCycleMapper;
import br.pucrs.estudoorganizado.repository.StudyCycleItemRepository;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCycleService {
    private static final Logger logger = LoggerFactory.getLogger(StudyCycleService.class);

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

        List<StudyCycleEntity> cycles = studyCycleRepository.findActivesCycles();

        response.cycles = cycles.stream()
                .map(StudyCycleMapper::convertToDTO)
                .toList();

        return response;

    }

}
