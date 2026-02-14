package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TopicStructureDTO {
    public Long id;
    public Long order;
    public String color;
    public String description;
    public String subject;
    public String annotation;
    public Long knowledgeScore;
    public Long incidenceScore;
    public String elapsedTime;
    public String score;
    public List<Long> reviewIntervals;

    private List<HistoryDTO> history = new ArrayList<>();
}
