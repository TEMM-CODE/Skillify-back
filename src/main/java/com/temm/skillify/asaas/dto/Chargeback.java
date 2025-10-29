package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chargeback {
    private String status;     // REQUESTED, IN_DISPUTE, WON, LOST, etc.
    private String reason;     // PROCESS_ERROR, FRAUD, etc.
}