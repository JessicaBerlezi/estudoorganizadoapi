package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectDTO create(InsertSubjectDTO dto) {

        SubjectEntity subject = SubjectMapper.convertToEntity(dto);
        subjectRepository.saveAndFlush(subject);
        logger.info("Subject saved: {}", subject);
        return SubjectMapper.convertToDTO(subject);
    }

    @Transactional
    public SubjectDTO update(Long subjectId, UpdateSubjectDTO dto) {

        SubjectEntity subject = subjectRepository.findByIdAndIsActiveTrue(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + subjectId));

        logger.info("Subject to be updated: {}, new info: {}", subject, dto);
        Map<Long, TopicEntity> existingTopics = subject.getTopics().stream()
                .collect(Collectors.toMap(TopicEntity::getId, Function.identity()));

        List<TopicEntity> updatedTopics = new ArrayList<>();
        int order = 1;
        for (UpdateTopicDTO topicDTO : dto.topics) {
            TopicEntity topic;
            if (topicDTO.id != null && existingTopics.containsKey(topicDTO.id)) {
                topic = existingTopics.get(topicDTO.id);
                topic = TopicMapper.update(topic,topicDTO, order++);
            } else {
                topic = TopicMapper.convertToEntity(topicDTO, subject, order++);
            }
            updatedTopics.add(topic);
        }

        SubjectEntity updated =  new SubjectEntity(
                subject,
                updatedTopics,
                dto.getDescription(),
                dto.getAnnotation()
        );
        logger.info("Subject updated: {}", updated);

        subjectRepository.save(updated);
        return SubjectMapper.convertToDTO(subject);
    }


    public List<SubjectDTO> findActivesSubjects() {
        return subjectRepository.findByIsActiveTrueOrderByIdAsc()
                .stream()
                .map(SubjectMapper::convertToDTO)
                .toList();
    }

    public SubjectDTO getActiveSubject(Long id) {
        SubjectEntity subject = subjectRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found or inactive: " + id));
        return SubjectMapper.convertToDTO(subject);
    }
}
