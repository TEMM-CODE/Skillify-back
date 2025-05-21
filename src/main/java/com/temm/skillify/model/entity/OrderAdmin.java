package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdmin extends com.temm.skillify.model.categories.BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesPlanAdminMembershipEvent salesPlanMembership;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    private LocalDateTime purchaseDate;
    private LocalDateTime expirationDate;
    
    private boolean active;
}
