package br.pucrs.estudoorganizado.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Tabela disciplina", description = """
        Tabela relacional de disciplina.\s
        Uma disciplina será pai de uma série de tópicos.\s
        Um disciplina poderá, ou não, fazer parte de um ou mais ciclos de estudo""")
@Entity
@Getter
@Table(name = "subject")
public class SubjectEntity  extends BaseCommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @Setter
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicEntity> topics = new ArrayList<>();

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

    public SubjectEntity() {
    }

    public SubjectEntity(String description, String annotation) {
        this.description = description;
        this.annotation = annotation;
    }

    //todo rever isso aqui
    public SubjectEntity(
            SubjectEntity entity,
            List<TopicEntity> topics,
            String description,
            String annotation) {
        this.id = entity.id;
        this.setCreatedAt(entity.getCreatedAt());
        this.setIsActive(entity.getIsActive());
        this.topics = topics;
        this.description = description;
        this.annotation = annotation;
        onUpdate();
    }

    public String toLogString() {
        String topicsLog = (topics == null || topics.isEmpty())
                ? "[]"
                : topics.stream()
                .map(TopicEntity::toLogString) // chama o toLogString de cada tópico
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format(
                "SubjectEntity{id=%d, description='%s', annotation='%s', topics[%S]=%s}",
                getId(),
                description,
                annotation,
                topics.size(),
                topicsLog
        );
    }
}
