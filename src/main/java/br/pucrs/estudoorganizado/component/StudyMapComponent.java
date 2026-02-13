package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyMapDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectDTO;
import br.pucrs.estudoorganizado.service.StudyMapService;
import br.pucrs.estudoorganizado.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class StudyMapComponent {

    private final StudyMapService service;
    private final SubjectService subjectService;

    private static final Logger logger = LoggerFactory.getLogger(StudyMapComponent.class);

    public StudyMapDTO getStudyMap() {
        try {
            return service.getStudyMaps();
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erro ao carregar mapa de estudos.");
        }
    }

    public SubjectDTO createSubjectWithTopics(InsertSubjectDTO dto) {
        try {
            return subjectService.createSubjectWithTopics(dto);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erro ao criar nova disciplina no mapa de estudos.");
        }
    }

    public SubjectDTO getSubjectById(Long subjectId) {
        try {
            return subjectService.getActiveSubject(subjectId);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erro ao carregar informações de disciplinas.");
        }
    }

    public SubjectDTO updateSubjectWithTopics(Long subjectId, UpdateSubjectDTO dto) {
        try {
            return subjectService.updateSubjectWithTopics(subjectId, dto);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erro ao atualizar dados de disciplinas.");
        }
    }

    public void deleteSubject(Long subjectId) {
        try {
            subjectService.deleteSubject(subjectId);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erro ao deletar dados de disciplinas.");
        }
    }
}
