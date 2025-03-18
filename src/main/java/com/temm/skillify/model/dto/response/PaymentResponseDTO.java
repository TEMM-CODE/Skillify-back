package com.temm.skillify.model.dto.response;

import com.temm.skillify.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String id;
    private BigDecimal amount;
    private String orderId;
    private String orderDetails;
    private String userId;
    private String userName;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime processingDate;
    private String transactionId;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}