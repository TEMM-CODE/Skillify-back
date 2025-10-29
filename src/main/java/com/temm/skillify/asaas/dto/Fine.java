package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fine {
    private BigDecimal value;
    private String type; // FIXED, PERCENTAGE
}