package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class CycleDTO {
    public String description; //first Line
    public String statusInfo; // top-right
    public String annotation; //second line
    public LinkedList<TopicDetailDTO> topics;

    public CycleDTO() {
    }

    public CycleDTO(String description, String statusInfo, String annotation, LinkedList<TopicDetailDTO> topics) {
        this.description = description;
        this.statusInfo = statusInfo;
        this.annotation = annotation;
        this.topics = topics;
    }
}
