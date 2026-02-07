package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.map.StudyRecordMapper;
import br.pucrs.estudoorganizado.repository.StudyCycleItemRepository;
import br.pucrs.estudoorganizado.repository.StudyRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudyRecordService {
    private final StudyCycleItemRepository itemRepository;
    private final StudyRecordRepository repository;



    public void create(Long cycleId, Long topicId, InsertStudyRecordDTO request) {
        StudyCycleItemEntity item = getStudyCycleItem(cycleId, topicId);
        repository.save(StudyRecordMapper.map(item, request));
    }

    private StudyCycleItemEntity getStudyCycleItem(Long cycleId, Long topicId) {
        return itemRepository.findByStudyCycleIdAndTopicId(cycleId, topicId)
                .orElseThrow(() -> new EntityNotFoundException("Study Cycle Item not found, cycle=" + cycleId + ", topicId=" +topicId));

    }
}
