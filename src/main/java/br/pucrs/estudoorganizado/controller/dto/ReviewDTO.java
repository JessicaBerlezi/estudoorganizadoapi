package br.pucrs.estudoorganizado.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class ReviewDTO {
    public String description;
    public String statusInfo;
    public List<TopicWithHistoryDTO> topics = new ArrayList<>();
}
