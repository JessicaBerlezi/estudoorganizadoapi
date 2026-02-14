package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;

public class StudyCycleMapper {

    public static StudyCycleEntity fromDTO(StudyCycleDetailsDTO dto) {
        return new StudyCycleEntity(
                dto.getDescription(),
                StudyStatusEnum.PLANNED,
                null,
                dto.getAnnotation(),
                null
        );
    }
}
