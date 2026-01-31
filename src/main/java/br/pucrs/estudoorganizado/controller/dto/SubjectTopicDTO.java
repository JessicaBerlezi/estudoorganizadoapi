package br.pucrs.estudoorganizado.controller.dto;

public class SubjectTopicDTO {

    public int idSubject;
    public int idTopic;

    public String toLogString() {
        return String.format("{idSubject=%d, idTopic=%d}", idSubject, idTopic);
    }
}
