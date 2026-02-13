package br.pucrs.estudoorganizado.controller.dto;


import lombok.Getter;

public class UpdateTopicDTO extends InsertTopicDTO {

    @Getter
    public Long id;

    public String toLogString() {
        return "Id="+ id +", " + super.toLogString();
    }
}
