package com.temm.skillify.model.dto.request;

import lombok.Data;
import com.temm.skillify.model.enums.PlanType;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesPlanAdminCreateDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private PlanType type;
    private List<String> resources;
}