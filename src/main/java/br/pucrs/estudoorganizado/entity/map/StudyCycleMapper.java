package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import br.pucrs.estudoorganizado.entity.view.CycleStudyView;

import java.time.LocalDateTime;

public class StudyCycleMapper {

    public static StudyCycleWithTopicsDTO toCycleDTO(CycleStudyView row) {
        StudyCycleWithTopicsDTO dto = new StudyCycleWithTopicsDTO();
        dto.setId(row.getCycleId());
        dto.setDescription(row.getCycleDescription());
        dto.setAnnotation(row.getCycleAnnotation());
        dto.setStatusInfo(buildStatusInfo(row.getCycleStartedAt()));
        return dto;
    }
    private static String buildStatusInfo(LocalDateTime startedAt) {
        return startedAt == null ? "Não iniciado" : "Iniciado em " + startedAt;
    }

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
