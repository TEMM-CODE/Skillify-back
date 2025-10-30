package com.temm.skillify.model.dto.request;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.temm.skillify.model.enums.AsaasEvent;

/**
 * DTO used when creating a new {@link com.temm.skillify.model.entity.SalesPlanPayment}.
 * Only the foreign-key id of the related membership event is required.
 */
@Data
public class SalesPlanPaymentCreateDTO {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expiresAt;
    private boolean confirmed;
    private boolean expired;
    private BigDecimal value;
     private String paymentLink;
      private AsaasEvent status;

    /** ID of the {@link com.temm.skillify.model.entity.SalesPlanMembershipEvent} */
    private String salesPlanMembershipEventId;
}