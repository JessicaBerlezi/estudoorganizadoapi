package br.pucrs.estudoorganizado.entity;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
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

    //@Column(name = "elapsed_time")
    //private Duration elapsedTime;

    //@Max(100)
    //private int score;

    /**
     * @ElementCollection
     * @CollectionTable( name = "topic_review_intervals",
     * joinColumns = @JoinColumn(name = "topic_id")
     * )
     * @Column(name = "interval_value")
     * private List<Integer> reviewIntervals = new ArrayList<>();
     */

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

    public TopicEntity() {
    }

    public TopicEntity(String description, Integer order, Integer incidenceScore, Integer knowledgeScore, String color, String annotation, SubjectEntity subject) {
        this.description = description;
        this.order = order;
        this.incidenceScore = incidenceScore;
        this.knowledgeScore = knowledgeScore;
        this.color = color;
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
