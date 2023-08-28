package com.cs.quizeloper.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Enumerated(EnumType.STRING)
    private BaseStatus status = BaseStatus.ACTIVE;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.MIN;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModifiedDate = LocalDateTime.MIN;
}
