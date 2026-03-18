package com.eandrade.domain.entity;

import com.eandrade.infrastructure.audit.AuditUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onPrePersist() {
        this.createdBy = AuditUtil.getCurrentUser();
        this.createdAt = LocalDateTime.now();
        this.status    = true;
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.updatedBy = AuditUtil.getCurrentUser();
        this.updatedAt = LocalDateTime.now();
    }
}