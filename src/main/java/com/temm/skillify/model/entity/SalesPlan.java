package com.temm.skillify.model.entity;


import com.temm.skillify.model.BaseEntity;
import com.temm.skillify.model.enums.PlanType;

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
public class SalesPlan extends BaseEntity {
    
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    private PlanType type;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> resources;
}