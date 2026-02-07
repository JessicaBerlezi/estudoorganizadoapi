package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.entity.TopicStudyHistoryView;

import java.time.LocalDateTime;

public class StudyCycleMapper {

    public static StudyCycleWithTopicsDTO toCycleDTO(TopicStudyHistoryView row) {
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
}
