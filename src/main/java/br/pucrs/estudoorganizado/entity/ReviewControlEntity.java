package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "review_control")
@Setter
@Getter
public class ReviewControlEntity extends BaseCommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_control_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "sequence_index")
    private Integer sequenceIndex;

    @Enumerated(EnumType.STRING)
    private ReviewStatusEnum status;
}
