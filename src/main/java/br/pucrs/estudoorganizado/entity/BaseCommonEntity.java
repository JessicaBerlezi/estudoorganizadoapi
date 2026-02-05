package br.pucrs.estudoorganizado.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseCommonEntity {

    @Column(name = "created_datetime", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updateBy;


    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
        this.createdBy = "system";
        this.updateBy = "system";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updateBy = "system";
    }
}
