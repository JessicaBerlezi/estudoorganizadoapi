package br.pucrs.estudoorganizado.controller.dto;

public class SubjectTopicOptionDTO {
    public Long id;
    public String description;
    public TopicSummaryDTO topic;

    public SubjectTopicOptionDTO(Long id, String description, TopicSummaryDTO topicDTO) {
        this.id = id;
        this.description = description;
        this.topic = topicDTO;
    }
}
