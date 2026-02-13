package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository repository;

    @Transactional
    public SubjectDTO createSubjectWithTopics(InsertSubjectDTO dto) {

        SubjectEntity subject = SubjectMapper.convertToEntity(dto);
        repository.save(subject);
        logger.info("Subject saved: {}", subject);
        return SubjectMapper.convertToDTO(subject);
    }

    @Transactional
    public SubjectEntity updateSubjectWithTopics(SubjectEntity updatedSubject) {
        try {
            return repository.save(updatedSubject);
        }catch (Exception e){
            logger.error("Erro ao atualizar registro de disciplina", e);
            throw  ApiExceptionFactory.badRequest(BusinessError.UPDATE_ERROR);
        }
    }


    public List<SubjectDTO> findActivesSubjects() {
        return repository.findByIsActiveTrueOrderByIdAsc()
                .stream()
                .map(SubjectMapper::convertToDTO)
                .toList();
    }

    public SubjectEntity getActiveSubject(Long id) {
        SubjectEntity subject = repository.findByIdWithTopics(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada " + id));

        if(!subject.getIsActive()){
            logger.error("Disciplina foi desativada.");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Disciplina inativa " + id);
        }
        return subject;
    }

    @Transactional
    public void disableSubject(Long id) {
        SubjectEntity entity = repository
                .findByIdWithTopics(id)
                .orElseThrow(() ->  ApiExceptionFactory.notFound(BusinessError.SUBJECT_NOT_FOUND));

        entity.setIsActive(false);
        entity.getTopics()
                .forEach(topic -> topic.setIsActive(false));
    }
}
