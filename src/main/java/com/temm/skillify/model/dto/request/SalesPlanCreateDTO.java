package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.temm.skillify.model.enums.PlanType;

@Data
public class SalesPlanCreateDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private PlanType type;
    private List<String> resources;
}