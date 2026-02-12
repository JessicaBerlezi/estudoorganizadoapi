package br.pucrs.estudoorganizado.entity.view;

import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Table(name = "vw_topic_history")
@Immutable
@Getter
public class TopicWithHistoryView {

    @Id
    private String id;

    private Long topicId;
    private Integer topicOrder;
    private String topicDescription;
    private Integer topicIncidenceScore;
    private Integer topicKnowledgeScore;
    private Long topicTotalDurationMinutes;
    private Integer topicAvgScore;
    private String subjectDescription;
    private String topicAnnotation;

    @Enumerated(EnumType.STRING)
    private StudyTypeEnum studyType;
    private LocalDate recordStartedAt;
    private Long recordDurationMinutes;
    private Integer questionsSolved;
    private Integer questionsIncorrected;
    private Double questionsPercent;
    private String recordAnnotation;
}
