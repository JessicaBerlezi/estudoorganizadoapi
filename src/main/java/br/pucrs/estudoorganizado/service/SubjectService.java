package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.map.SubjectMapper;
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

    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectDTO create(InsertSubjectDTO dto) {

        SubjectEntity subject = SubjectMapper.convertToEntity(dto);
        subjectRepository.saveAndFlush(subject);
        logger.info("Subject saved: {}", subject);
        return SubjectMapper.convertToDTO(subject);
    }

    public SubjectDTO getSubjectById(Long id) {
        SubjectEntity subject =  subjectRepository.getReferenceById(id);
        logger.info("Subject find: {}", subject.toLogString());
        return SubjectMapper.convertToDTO(subject);
    }
}
