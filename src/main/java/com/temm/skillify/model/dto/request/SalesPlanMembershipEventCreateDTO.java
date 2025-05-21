package com.temm.skillify.model.dto.request;

import lombok.Data;
import com.temm.skillify.model.enums.SalesPlanMembershipType;

import java.util.UUID;

@Data
public class SalesPlanMembershipEventCreateDTO {
    private String customerId;
    private String salesPlanId;
    private SalesPlanMembershipType status;
}