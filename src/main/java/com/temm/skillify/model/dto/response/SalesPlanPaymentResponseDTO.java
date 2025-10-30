package com.temm.skillify.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.temm.skillify.model.enums.AsaasEvent;

/**
 * Full response DTO returned to the client after a {@link com.temm.skillify.model.entity.SalesPlanPayment}
 * is read or created.
 */
@Data
public class SalesPlanPaymentResponseDTO {

    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expiresAt;
    private boolean confirmed;
    private boolean expired;
    private BigDecimal value;
 private String paymentLink;
    /** Nested DTO for the related membership event */
    private SalesPlanMembershipEventReturnDTO salesPlanMembershipEvent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     private AsaasEvent status;
}