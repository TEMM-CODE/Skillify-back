package com.temm.skillify.model.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.temm.skillify.model.enums.AsaasEvent;

import lombok.Data;

@Data
public class SalesPlanAdminPaymentCreateDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expiresAt;
    private boolean confirmed;
    private boolean expired;
    private BigDecimal value;
    private String salesPlanAdminMembershipEventId;
    private String paymentLink;
     private AsaasEvent status;
}