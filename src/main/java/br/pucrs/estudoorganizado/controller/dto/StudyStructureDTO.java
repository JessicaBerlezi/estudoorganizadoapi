package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudyStructureDTO {
    private Long id;
    private Long order;
    private String description;
    private String statusInfo;
    private String annotation;
    private List<TopicStructureDTO> topics = new ArrayList<>();
}
