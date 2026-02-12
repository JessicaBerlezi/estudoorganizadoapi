package br.pucrs.estudoorganizado.controller.dto;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class ReviewWithTopicsDTO {
    public String description;
    public String statusInfo;
    public List<TopicWithHistoryDTO> topics = new ArrayList<>();
}
