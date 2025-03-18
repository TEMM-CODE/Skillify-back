package com.temm.skillify.model.entity;


import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    
    private BigDecimal amount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    private LocalDateTime paymentDate;
    private LocalDateTime processingDate;
    
    private String transactionId;
    private String paymentMethod;
}