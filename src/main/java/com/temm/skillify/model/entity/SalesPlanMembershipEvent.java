package com.temm.skillify.model.entity;
import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.model.enums.SalesPlanMembershipType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class SalesPlanMembershipEvent  extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private User customer;

   @ManyToOne(fetch = FetchType.EAGER)
   private SalesPlan salesPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesPlanMembershipType status;
}
