package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public SubjectDTO createSubjectWithTopics(InsertSubjectDTO dto) {

        SubjectEntity subject = SubjectMapper.convertToEntity(dto);
        subjectRepository.saveAndFlush(subject);
        logger.info("Subject saved: {}", subject);
        return SubjectMapper.convertToDTO(subject);
    }

    @Transactional
    public SubjectDTO updateSubjectWithTopics(Long subjectId, UpdateSubjectDTO dto) {

        SubjectEntity subject = subjectRepository.findByIdAndIsActiveTrue(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada " + subjectId));

        logger.info("Subject to be updated: {}, new info: {}", subject, dto);
        Map<Long, TopicEntity> existingTopics = subject.getTopics().stream()
                .collect(Collectors.toMap(TopicEntity::getId, Function.identity()));

        List<TopicEntity> updatedTopics = new ArrayList<>();
        int order = 1;
        for (UpdateTopicDTO topicDTO : dto.topics) {
            TopicEntity topic;
            if (topicDTO.id != null && existingTopics.containsKey(topicDTO.id)) {
                topic = existingTopics.get(topicDTO.id);
                topic = new TopicEntity(
                        topic,
                        topicDTO.description,
                        order++,
                        topicDTO.incidenceScore,
                        topicDTO.knowledgeScore,
                        topicDTO.reviewIntervals,
                        topicDTO.annotation
                );
            } else {
                topic = TopicMapper.convertToEntity(topicDTO, subject, order++);
            }
            updatedTopics.add(topic);
        }

        SubjectEntity updated = new SubjectEntity(
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada " + id));
        return SubjectMapper.convertToDTO(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
