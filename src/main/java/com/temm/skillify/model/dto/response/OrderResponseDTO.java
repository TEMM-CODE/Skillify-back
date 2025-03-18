package com.temm.skillify.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private String id;
    private String salesPlanId;
    private String salesPlanName;
    private String userId;
    private String userName;
    private LocalDateTime purchaseDate;
    private LocalDateTime expirationDate;
    private boolean active;
    private BigDecimal price;
}
