package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class TopicReviewDetailDTO extends  TopicDetailDTO {
    public String reviewInfo;
    public LinkedList<TopicHistoryDTO> history;
}
