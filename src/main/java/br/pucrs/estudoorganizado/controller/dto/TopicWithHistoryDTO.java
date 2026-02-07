package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TopicWithHistoryDTO extends TopicSummaryDTO {
    public String subject;
    public String annotation;
    private List<TopicHistoryDTO> history = new ArrayList<>();
}
