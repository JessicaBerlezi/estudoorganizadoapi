package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyMapService {

    private final SubjectService service;

    public StudyMapService(SubjectService service) {
        this.service = service;
    }

    public StudyMapDTO getStudyMaps() {
        StudyMapDTO studyMapsDTO = new StudyMapDTO();
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
