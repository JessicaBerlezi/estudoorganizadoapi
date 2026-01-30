package br.pucrs.estudoorganizado.controller.dto;

public class TopicDetailDTO extends  TopicSummaryDTO {
    //color area
    public String description; //first Line
    public String subject; //second Line
    //gray area
    public String annotation; //last line, could be more than one line
}
