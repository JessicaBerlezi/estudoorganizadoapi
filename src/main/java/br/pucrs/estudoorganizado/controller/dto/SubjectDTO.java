package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class SubjectDTO {
    public Integer order;
    public String description; //second line, smaller font size
    public String statusInfo; // top-right
    public String annotation;
    public LinkedList<TopicSummaryDTO> topics;
}
