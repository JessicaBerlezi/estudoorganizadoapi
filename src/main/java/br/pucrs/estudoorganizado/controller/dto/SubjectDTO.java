package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class SubjectDTO {
    public Long id;
    public Integer order;
    public String description; //second line, smaller font size
    public String statusInfo; // top-right
    public String annotation;
    public LinkedList<TopicSummaryDTO> topics;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<TopicSummaryDTO> getTopics() {
        return topics;
    }
}
