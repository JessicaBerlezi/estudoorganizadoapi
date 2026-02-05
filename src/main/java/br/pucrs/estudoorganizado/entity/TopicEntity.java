package br.pucrs.estudoorganizado.entity;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Tabela tópicos de uma disciplina", description = """
        Tabela relacional de tópicos de uma disciplina.\s
        Pertencerá a uma disciplina, e será destruído com ela.\s
        Cor é uma cor calculada a partir dos dados da própria tabela sobre conhecimento anterior e incidência em probas. \s
        Tempo decorrido e score serão valores calculados a partir de registros de estudo relacionado ao tópico""")
@Getter
@Entity
@Table(name = "topic")
public class TopicEntity extends BaseCommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @Column(name = "topic_order")
    private Integer order;

    @Column(name = "incidence_score")
    private Integer incidenceScore;

    @Column(name = "knowledge_score")
    private Integer knowledgeScore;

    @Size(max = 7)
    @Column(length = 7)
    private String color;

    @ElementCollection
    @CollectionTable(name = "topic_review_days", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "day_of_week")
    private List<Integer> reviewIntervalsDays = new ArrayList<>();

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

    public TopicEntity() {
    }

    public TopicEntity(String description,
                       Integer order,
                       Integer incidenceScore,
                       Integer knowledgeScore,
                       String color,
                       List<Integer> reviewIntervalsDays,
                       String annotation,
                       SubjectEntity subject) {
        this.description = description;
        this.order = order;
        this.incidenceScore = incidenceScore;
        this.knowledgeScore = knowledgeScore;
        this.color = color;
        this.reviewIntervalsDays = reviewIntervalsDays;
        this.annotation = annotation;
        this.subject = subject;
    }

    public String toLogString() {
        return String.format(
                "TopicEntity{id=%d, order=%d, description='%s', incidenceScore=%d, knowledgeScore=%d, color='%s', annotation='%s'}",
                getId(),
                order,
                description,
                incidenceScore,
                knowledgeScore,
                color,
                annotation
        );
    }
}
