package br.pucrs.estudoorganizado.entity.view;

import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "vw_study_structure ")
public class StudyStructureView {

    @Id
    private String id;

    private Long subjectId;
    private Long subjectOrder;
    private String subjectDescription;
    private String subjectAnnotation;

    private Long topicId;
    private Long topicOrder;
    private String topicDescription;
    private String topicAnnotation;
    private String topicReviewDays;

    private Integer topicIncidenceScore;
    private Integer topicKnowledgeScore;

    private Long studyCycleId;
    private String cycleDescription;
    private String cycleAnnotation;
    @Enumerated(EnumType.STRING)
    private StudyStatusEnum cycleStatus;
    private LocalDateTime cycleStartedAt;

    private LocalDate reviewScheduleDate;
    @Enumerated(EnumType.STRING)
    private ReviewStatusEnum reviewStatus;

    private Long studyRecordId;
    @Enumerated(EnumType.STRING)
    private StudyTypeEnum studyType;
    private LocalDate studyStartedAt;
    private Long studyDurationMinutes;
    private Integer studyQuestionsSolved;
    private Integer studyQuestionsIncorrected;
    private String studyAnnotation;
}
