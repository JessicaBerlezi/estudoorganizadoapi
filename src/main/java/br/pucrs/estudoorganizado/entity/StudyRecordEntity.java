package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "study_record")
public class StudyRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_record_id")
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "study_cycle_item_id")
    private final StudyCycleItemEntity studyCycleItem;

    @ManyToOne(optional = true)
    @JoinColumn(name = "review_control_id")
    private final ReviewControlEntity reviewControl;

    @Enumerated(EnumType.STRING)
    private final StudyTypeEnum studyType;

    private final LocalDate startedAt;

    @Column(name = "duration_minutes")
    private final Long minutes;

    private final double questionsPercent;

    private final Integer questionsSolved;

    private final Integer questionsIncorrected;

    @Size(max = 250)
    @Column(length = 250)
    private final String annotation;

    public StudyRecordEntity(StudyCycleItemEntity studyCycleItem,
                             ReviewControlEntity reviewControl,
                             StudyTypeEnum studyType,
                             LocalDate startedAt,
                             Long minutes,
                             double questionsPercent,
                             Integer questionsSolved,
                             Integer questionsIncorrected,
                             String annotation) {
        this.studyCycleItem = studyCycleItem;
        this.reviewControl = reviewControl;
        this.studyType = studyType;
        this.startedAt = startedAt;
        this.minutes = minutes;
        this.questionsPercent = questionsPercent;
        this.questionsSolved = questionsSolved;
        this.questionsIncorrected = questionsIncorrected;
        this.annotation = annotation;
    }
}

