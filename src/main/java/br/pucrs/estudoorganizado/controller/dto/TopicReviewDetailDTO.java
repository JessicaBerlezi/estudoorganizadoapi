package br.pucrs.estudoorganizado.controller.dto;

import java.util.LinkedList;

public class TopicReviewDetailDTO extends  TopicDetailDTO {
    //color area
    public String reviewInfo; //third line - smaller font size, example: Agenda de próximas revsões: 3d, 5d, 15d, 30d
    //gray area
    public LinkedList<TopicHistoryDTO> history; // 2 columns info, before annotation
}
