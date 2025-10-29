package com.temm.skillify.model.entity;

import java.time.LocalDateTime;

import com.temm.skillify.model.categories.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "webhook_events", uniqueConstraints = @UniqueConstraint(columnNames = "event_id"))
@Data
public class ProcessedAsaasEvent extends BaseEntity{

    @Column(name = "event_id", nullable = false, length = 128)
    private String eventId;

    private LocalDateTime processedAt = LocalDateTime.now();
}