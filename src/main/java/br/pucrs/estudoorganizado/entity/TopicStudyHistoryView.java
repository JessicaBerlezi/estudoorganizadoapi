package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "vw_topic_study_history")
@Getter
public class TopicStudyHistoryView {

    @Id
    private String id;

    private Long cycleId;
    private String cycleDescription;
    private String cycleAnnotation;
    private LocalDateTime cycleStartedAt;

    private Long topicId;
    private String topicDescription;
    private Integer topicIncidenceScore;
    private Integer topicKnowledgeScore;
    private Integer topicOrder;
    private String topicAnnotation;
    private String subjectDescription;

    private Long recordId;
    private LocalDate recordStartedAt;
    private Long recordDurationMinutes;
    private Integer questionsSolved;
    private Integer questionsIncorrected;
    private Double questionsPercent;
    private String recordAnnotation;

    private Long topicTotalDurationMinutes;
    private Integer topicAvgScore;
    @Enumerated(EnumType.STRING)
    private StudyTypeEnum studyType;


}
