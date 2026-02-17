package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository repository;

    @Transactional
    public SubjectEntity createSubjectWithTopics(InsertSubjectStructureDTO dto) {
        SubjectEntity subject = SubjectMapper.createSubjectAndConvertToEntity(dto);
        SubjectEntity entity = repository.save(subject);
        logger.info("Subject saved: {}", subject);
        return entity;
    }

    @Transactional
    public SubjectEntity updateSubjectWithTopics(SubjectEntity updatedSubject) {
        try {
            return repository.save(updatedSubject);
        }catch (Exception e){
            logger.error("Erro ao atualizar registro de disciplina", e);
            throw ApiExceptionFactory.notFound(BusinessError.STUDY_MAP_UPDATE);
        }
    }
    public SubjectEntity getActiveSubject(Long id) {
        SubjectEntity subject = repository.findByIdWithTopics(id)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.SUBJECT_LOAD));

        if(!subject.getIsActive()){
            logger.error("Disciplina foi desativada.");
            throw ApiExceptionFactory.conflict(BusinessError.SUBJECT_LOAD);
        }
        return subject;
    }

    @Transactional
    public void disableSubject(Long id) {
        SubjectEntity entity = repository
                .findByIdWithTopics(id)
                .orElseThrow(() ->  ApiExceptionFactory.notFound(BusinessError.SUBJECT_LOAD));

        entity.setIsActive(false);
        entity.getTopics()
                .forEach(topic -> topic.setIsActive(false));
    }
}
