package br.pucrs.estudoorganizado.controller.dto;

import lombok.Getter;

@Getter
public class SubjectTopicDTO {

    public Long idSubject;
    public Long idTopic;

    public String toLogString() {
        return String.format("{idSubject=%d, idTopic=%d}", idSubject, idTopic);
    }
}
