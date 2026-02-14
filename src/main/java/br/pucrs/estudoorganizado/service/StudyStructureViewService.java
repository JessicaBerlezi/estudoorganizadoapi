package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.StudyStructureDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicStructureDTO;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.entity.map.StudyStructureViewMapper;
import br.pucrs.estudoorganizado.entity.view.StudyStructureView;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.StudyStructureViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyStructureViewService {

    private final StudyStructureViewRepository repository;

    public List<StudyStructureDTO> findActiveSubjectsWithFullTopicHistory() {
        List<StudyStructureView> rows = repository.findAll();

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.notFound(BusinessError.STUDY_MAP_LOAD);
        }

        Map<Long, StudyStructureDTO> subjectMap = new LinkedHashMap<>();
        Map<Long, TopicStructureDTO> topicMap = new HashMap<>();

        for (StudyStructureView row : rows) {

            StudyStructureDTO subject = subjectMap.computeIfAbsent(
                    row.getSubjectId(),
                    id -> StudyStructureViewMapper.buildSubjectInfo(row)
            );

            topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicStructureDTO t = StudyStructureViewMapper.buildTopicInfo(row);
                        subject.getTopics().add(t);
                        return t;
                    }
            );
        }
        return new ArrayList<>(subjectMap.values());
    }


    public StudyStructureDTO getActiveSubjectWithFullTopicHistory(Long subjectId) {
        List<StudyStructureView> rows = repository.findBySubjectId(subjectId);

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.notFound(BusinessError.SUBJECT_LOAD);
        }

        StudyStructureDTO subject = StudyStructureViewMapper.buildSubjectInfo(rows.getFirst());
        Map<Long, TopicStructureDTO> topicMap = new HashMap<>();

        for (StudyStructureView row : rows) {

            topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicStructureDTO t = StudyStructureViewMapper.buildTopicInfo(row);
                        subject.getTopics().add(t);
                        return t;
                    }
            );
        }
        return subject;
    }


    public List<StudyStructureDTO> findActiveStudyCycleWithFullTopicHistory() {
        List<StudyStructureView> rows = repository.findAll();

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD);
        }

        Map<Long, StudyStructureDTO> cycleMap = new LinkedHashMap<>();
        Map<Long, TopicStructureDTO> topicMap = new HashMap<>();

        for (StudyStructureView row : rows) {

            StudyStructureDTO cycle = cycleMap.computeIfAbsent(
                    row.getStudyCycleId(),
                    id -> StudyStructureViewMapper.buildStudyCycleInfo(row)
            );

            topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicStructureDTO t = StudyStructureViewMapper.buildTopicInfo(row);
                        cycle.getTopics().add(t);
                        return t;
                    }
            );
        }
        return new ArrayList<>(cycleMap.values());
    }


    public StudyStructureDTO getActiveCycleWithFullTopicHistory(Long cycleId) {
        List<StudyStructureView> rows = repository.findByStudyCycleId(cycleId);

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD);
        }

        StudyStructureDTO cycle = StudyStructureViewMapper.buildSubjectInfo(rows.getFirst());
        Map<Long, TopicStructureDTO> topicMap = new HashMap<>();

        for (StudyStructureView row : rows) {

            topicMap.computeIfAbsent(
                    row.getTopicId(),
                    id -> {
                        TopicStructureDTO t = StudyStructureViewMapper.buildTopicInfo(row);
                        cycle.getTopics().add(t);
                        return t;
                    }
            );
        }
        return cycle;
    }
}
