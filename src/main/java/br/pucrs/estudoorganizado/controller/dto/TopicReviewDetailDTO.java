package br.pucrs.estudoorganizado.controller.dto;

import lombok.Setter;
import java.util.List;

@Setter
public class TopicReviewDetailDTO extends TopicDetailDTO {
    public String reviewInfo;
    public List<TopicHistoryDTO> history;
}
