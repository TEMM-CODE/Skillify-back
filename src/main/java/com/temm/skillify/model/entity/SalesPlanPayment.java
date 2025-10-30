package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.AsaasEvent;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SalesPlanPayment extends BaseEntity{

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expiresAt;
    private boolean confirmed;
    private boolean expired;
    private BigDecimal value;
    private String paymentLink;
     private AsaasEvent status;
    @ManyToOne
    @JoinColumn(nullable = false)
    private SalesPlanMembershipEvent salesPlanMembershipEvent;

    
}
