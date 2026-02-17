package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.RegisterStudyRecordDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.entity.map.StudyRecordMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.StudyCycleItemRepository;
import br.pucrs.estudoorganizado.repository.StudyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudyRecordService {
    private final StudyCycleItemRepository itemRepository;
    private final StudyRecordRepository repository;
    private final ReviewControlService reviewControlService;


    public void recordStudy(Long cycleId, Long topicId, RegisterStudyRecordDTO request) {
        StudyCycleItemEntity item = getStudyCycleItem(cycleId, topicId);
        repository.save(StudyRecordMapper.toStudyRecord(item, request));

        if(request.getIsDone() == true) {
            reviewControlService.startedReview(topicId);
        }
    }

    private StudyCycleItemEntity getStudyCycleItem(Long cycleId, Long topicId) {
        return itemRepository.findByStudyCycleIdAndTopicId(cycleId, topicId)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD));

    }
}
