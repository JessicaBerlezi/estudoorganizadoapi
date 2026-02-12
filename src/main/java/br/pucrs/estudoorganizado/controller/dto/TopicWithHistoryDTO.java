package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TopicWithHistoryDTO extends TopicSummaryDTO {
    public String subject;
    public String annotation;
    private List<HistoryDTO> history = new ArrayList<>();
}
