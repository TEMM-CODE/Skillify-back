package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Refund {
    private String id;
    private BigDecimal value;
    private LocalDate dateCreated;
    private String status;     // REQUESTED, IN_PROGRESS, DONE, etc.
    private String description;
}