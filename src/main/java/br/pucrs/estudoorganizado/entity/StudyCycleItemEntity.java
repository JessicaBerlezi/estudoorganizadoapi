package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Tag(name = "Tabela relacionamento N:N entre Tópico e Ciclo de estudos ", description = """
        Muitos itens (tópicos) podem pertencer a um ciclo de estudo""")

@Entity
@Table(name = "study_cycle_item")
@Getter
@Setter
@RequiredArgsConstructor
public class StudyCycleItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_cycle_item_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "study_cycle_id")
    private StudyCycleEntity studyCycle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @Enumerated(EnumType.STRING)
    private StudyStatusEnum status;

    private LocalDateTime startedAt;
    private LocalDateTime doneAt;

    public StudyCycleItemEntity(StudyCycleEntity cycle, TopicEntity topic) {
        this.studyCycle = cycle;
        this.topic = topic;
        this.status = StudyStatusEnum.PLANNED;
        this.startedAt = null;
        this.doneAt = null;
    }
}
