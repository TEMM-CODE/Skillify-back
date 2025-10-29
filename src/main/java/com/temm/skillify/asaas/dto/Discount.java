package com.temm.skillify.asaas.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discount {
    private BigDecimal value;
    private Integer dueDateLimitDays;
    private LocalDate limitedDate;
    private String type; // FIXED, PERCENTAGE
}