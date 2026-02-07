package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.RegistreStudyRecordDTO;
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
    private final ReviewControlService reviewControlService;


    public void recordStudy(Long cycleId, Long topicId, RegistreStudyRecordDTO request) {
        StudyCycleItemEntity item = getStudyCycleItem(cycleId, topicId);
        repository.save(StudyRecordMapper.toStudyRecord(item, request));

        if(request.getIsDone() == true) {
            reviewControlService.startedReview(topicId);
        }
    }

    private StudyCycleItemEntity getStudyCycleItem(Long cycleId, Long topicId) {
        return itemRepository.findByStudyCycleIdAndTopicId(cycleId, topicId)
                .orElseThrow(() -> new EntityNotFoundException("Study Cycle Item not found, cycle=" + cycleId + ", topicId=" +topicId));

    }
}
