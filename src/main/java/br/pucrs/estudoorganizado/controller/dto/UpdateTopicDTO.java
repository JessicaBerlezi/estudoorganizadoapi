package br.pucrs.estudoorganizado.controller.dto;


public class UpdateTopicDTO extends InsertTopicDTO {

    public Long id;

    public String toLogString() {
        return "Id="+ id +", " + super.toLogString();
    }
}
