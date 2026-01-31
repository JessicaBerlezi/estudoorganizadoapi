package br.pucrs.estudoorganizado.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseCommonEntity {


    @Column(name = "created_datetime", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = "system"; // depois pode vir do usuário logado
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
