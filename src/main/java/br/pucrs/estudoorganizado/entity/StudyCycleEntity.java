package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.entity.enumerate.StudyCycleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_cycle")
public class StudyCycleEntity extends BaseCommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudyCycleStatus status;

    @Column(name = "started_datetime_")
    private LocalDateTime startedAt;

    public StudyCycleEntity(String description, String annotation, StudyCycleStatus status, LocalDateTime startedAt) {
        this.description = description;
        this.annotation = annotation;
        this.status = status;
        this.startedAt = startedAt;
    }

    public static StudyCycleEntity fromDTO(InsertStudyCycleDTO dto, LocalDateTime startedAt) {
        return new StudyCycleEntity(
                dto.getDescription(),
                dto.getAnnotation(),
                StudyCycleStatus.CREATED,
                startedAt
        );
    }
}
