package com.temm.skillify.model.dto.request;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String userId;
    private String planId;
    private Integer months;  // Optional for AdminOrderService
}