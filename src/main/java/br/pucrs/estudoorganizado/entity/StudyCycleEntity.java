package br.pucrs.estudoorganizado.entity;

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

    public StudyCycleEntity(String description, StudyStatusEnum status, LocalDateTime startedAt, String annotation, List<StudyCycleItemEntity> items) {
        this.description = description;
        this.status = status;
        this.startedAt = startedAt;
        this.annotation = annotation;
        this.items = items;
        onCreate();
    }
}
