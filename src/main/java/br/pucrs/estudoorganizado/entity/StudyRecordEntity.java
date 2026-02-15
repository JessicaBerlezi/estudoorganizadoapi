package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.entity.enumerate.StudyTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_record")
public class StudyRecordEntity  extends BaseCommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_record_id")
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "study_cycle_item_id")
    private StudyCycleItemEntity studyCycleItem;

    @ManyToOne(optional = true)
    @JoinColumn(name = "review_control_id")
    private ReviewControlEntity reviewControl;

    @Enumerated(EnumType.STRING)
    private StudyTypeEnum studyType;

    private LocalDate startedAt;

    @Column(name = "duration_minutes")
    private Long minutes;

    private double questionsPercent;

    private Integer questionsSolved;

    private  Integer questionsIncorrected;

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

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

