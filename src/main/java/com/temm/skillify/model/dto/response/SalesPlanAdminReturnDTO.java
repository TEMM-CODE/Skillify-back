package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.temm.skillify.model.enums.PlanType;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesPlanAdminReturnDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private PlanType type;
    private List<String> resources;
    private List<SalesPlanAdminMembershipEventReturnDTO> membershipEvents;
}