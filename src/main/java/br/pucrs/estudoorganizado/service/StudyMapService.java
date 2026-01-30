package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.UpdateOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicOrderDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyMapService {



    public void updateSubjectsOrder(UpdateSubjectOrderDTO request) {
        validatedObjectOrderParams(request.subjectsOrder);
    }

    public void updateTopicsOrder(UpdateTopicOrderDTO request) {
        validatedObjectOrderParams(request.subjectsOrder);
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
