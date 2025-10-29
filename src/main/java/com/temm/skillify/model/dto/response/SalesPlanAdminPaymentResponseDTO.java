package com.temm.skillify.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SalesPlanAdminPaymentResponseDTO {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expiresAt;
    private boolean confirmed;
    private boolean expired;
    private BigDecimal value;
    private SalesPlanAdminMembershipEventReturnDTO salesPlanAdminMembershipEvent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}