package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class ReviewDTO {
    public String description; //first Line
    public String statusInfo; // top-right
    public LinkedList<TopicReviewDetailDTO> topics;

    public ReviewDTO() {
    }

    public ReviewDTO(String description, String statusInfo, LinkedList<TopicReviewDetailDTO> topics) {
        this.description = description;
        this.statusInfo = statusInfo;
        this.topics = topics;
    }
}
