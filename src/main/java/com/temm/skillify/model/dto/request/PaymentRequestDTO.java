package com.temm.skillify.model.dto.request;

import com.temm.skillify.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private BigDecimal amount;
    private String orderId;
    private String userId;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
}