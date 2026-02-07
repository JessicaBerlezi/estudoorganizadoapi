package br.pucrs.estudoorganizado.controller.dto;

import lombok.Data;

@Data
public class TopicSummaryDTO {
    public long id;
    public Integer order;
    public String color;
    public String description;
    public String elapsedTime;
    public String score;
}
