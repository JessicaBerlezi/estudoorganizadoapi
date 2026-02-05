package br.pucrs.estudoorganizado.entity.map;

import br.pucrs.estudoorganizado.controller.dto.CycleDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicDetailDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class StudyCycleMapper {

    public static CycleDTO convertToDTO(StudyCycleEntity cycle) {

        LinkedList<TopicDetailDTO> topicDTOs = cycle.getItems()
                .stream()
                .map(StudyCycleItemEntity::getTopic)
                .map(TopicMapper::convertToDetailDTO)
                .collect(Collectors.toCollection(LinkedList::new));

        return new CycleDTO(
                cycle.getDescription(),
                buildStatusInfo(cycle),
                cycle.getAnnotation(),
                topicDTOs
        );

    }

    private static String buildStatusInfo(StudyCycleEntity cycle) {
        long total = cycle.getItems().size();
        long done = cycle.getItems().stream()
                .filter(i -> i.getDoneAt() != null)
                .count();

        return done + "/" + total + " concluídos";
    }
}
