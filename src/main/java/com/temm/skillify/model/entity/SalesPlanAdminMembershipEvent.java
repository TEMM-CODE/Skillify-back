package com.temm.skillify.model.entity;

import java.util.List;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.SalesPlanMembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SalesPlanAdminMembershipEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private SalesPlanAdmin salesPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesPlanMembershipType status;

    @OneToMany(mappedBy = "salesPlanAdminMembershipEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<SalesPlanAdminPayment> salesPlanAdminPayments;


}