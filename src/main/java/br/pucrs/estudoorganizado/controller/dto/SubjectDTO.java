package br.pucrs.estudoorganizado.controller.dto;

import lombok.Getter;

import java.util.LinkedList;

@Getter
public class SubjectDTO {
    public Long id;
    public String description;
    public LinkedList<TopicSummaryDTO> topics;
}
