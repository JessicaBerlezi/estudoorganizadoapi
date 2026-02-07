package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class StudyCycleWithTopicsDTO {
    private Long id;
    private String description;
    private String statusInfo;
    private String annotation;
    private List<TopicWithHistoryDTO> topics = new ArrayList<>();
}
