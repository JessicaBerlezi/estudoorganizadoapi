package br.pucrs.estudoorganizado.entity;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_cycle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyCycleEntity extends BaseCommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_cycle_id")
    private Long id;

    @NotBlank
    //@Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyStatusEnum status;

    @Column(name = "started_datetime")
    private LocalDateTime startedAt;

    @Size(max = 250)
    @Column(length = 250)
    private String annotation;

    @OneToMany(mappedBy = "studyCycle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyCycleItemEntity> items = new ArrayList<>();

    private StudyCycleEntity(String description, String annotation, StudyStatusEnum status, LocalDateTime startedAt) {
        this.description = description;
        this.status = status;
        this.startedAt = startedAt;
        this.annotation = annotation;
    }

    public static StudyCycleEntity fromDTO(InsertStudyCycleDTO dto) {
        return new StudyCycleEntity(
                dto.getDescription(),
                dto.getAnnotation(),
                StudyStatusEnum.PLANNED,
                null
        );
    }
}
