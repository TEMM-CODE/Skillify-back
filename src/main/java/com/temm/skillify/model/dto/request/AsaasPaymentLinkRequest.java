package com.temm.skillify.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsaasPaymentLinkRequest {

    @NotBlank(message = "name is required")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("endDate")
    private String endDate; // ISO date string, e.g., "2025-12-31"

    @JsonProperty("value")
    @PositiveOrZero(message = "value must be zero or positive")
    private BigDecimal value;

    @NotNull(message = "billingType is required")
    @JsonProperty("billingType")
    private BillingType billingType;

    @NotNull(message = "chargeType is required")
    @JsonProperty("chargeType")
    private ChargeType chargeType;

    @JsonProperty("dueDateLimitDays")
    @Positive(message = "dueDateLimitDays must be positive")
    private Integer dueDateLimitDays;

    @JsonProperty("subscriptionCycle")
    private SubscriptionCycle subscriptionCycle;

    @JsonProperty("maxInstallmentCount")
    @Min(value = 1, message = "maxInstallmentCount must be at least 1")
    @Builder.Default
    private Integer maxInstallmentCount = 1;

    @JsonProperty("externalReference")
    private String externalReference;

    @JsonProperty("notificationEnabled")
    @Builder.Default
    private Boolean notificationEnabled = true;

    @Valid
    @JsonProperty("callback")
    private Callback callback;

    // Enums

    public enum BillingType {
        UNDEFINED, BOLETO, CREDIT_CARD, PIX
    }

    public enum ChargeType {
        DETACHED, RECURRENT, INSTALLMENT
    }

    public enum SubscriptionCycle {
        WEEKLY, BIWEEKLY, MONTHLY, BIMONTHLY, QUARTERLY, SEMIANUALLY, YEARLY
    }

    // Nested Callback class

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Callback {

        @NotBlank(message = "successUrl is required")
        @JsonProperty("successUrl")
        private String successUrl;

        @JsonProperty("autoRedirect")
        @Builder.Default
        private Boolean autoRedirect = true;
    }
}