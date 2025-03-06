package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;
import com.temm.skillify.model.enums.PlanType;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesPlanResponseDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private PlanType type;
    private List<String> resources;
}
