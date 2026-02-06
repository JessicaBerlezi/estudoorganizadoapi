package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyMapService {

    private static final Logger logger = LoggerFactory.getLogger(StudyMapService.class);

    private final SubjectService service;

    public StudyMapService(SubjectService service) {
        this.service = service;
    }

    public StudyMapsDTO getStudyMaps() {
        StudyMapsDTO studyMapsDTO = new StudyMapsDTO();
        studyMapsDTO.subjects = service.findActivesSubjects();
        return studyMapsDTO;
    }

    public void updateSubjectsOrder(UpdateSubjectOrderDTO request) {
        validatedObjectOrderParams(request.subjectsOrder);
    }

    public void updateTopicsOrder(UpdateTopicOrderDTO request) {
        validatedObjectOrderParams(request.topicsOrder);
    }

    private void validatedObjectOrderParams(List<UpdateOrderDTO> subjectsOrder){
        int size = subjectsOrder.size();

        int maxOrder = subjectsOrder.stream()
                .mapToInt(s -> s.order)
                .max()
                .orElse(0);

        if (maxOrder > size) {
            throw new IllegalArgumentException("Order value cannot be greater than list size");
        }
    }
}
