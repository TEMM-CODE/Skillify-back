package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Split {
    private String id;
    private String walletId;
    private BigDecimal fixedValue;        // if fixed split
    private BigDecimal percentualValue;   // if percentage split
    private String status;                // PENDING, PAID, etc.
    private String refusalReason;
    private String externalReference;
    private String description;
}