package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.temm.skillify.model.enums.SalesPlanMembershipType;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesPlanAdminMembershipEventReturnDTO extends BaseResponseDTO {
    private String customerId;
    private String salesPlanId;
    private SalesPlanMembershipType status;
}