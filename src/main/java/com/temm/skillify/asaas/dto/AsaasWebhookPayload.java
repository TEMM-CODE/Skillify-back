package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsaasWebhookPayload {

    private String id;         
    private String event;            
    private String dateCreated;

    @JsonProperty("payment")
    private AsaasPaymentPayload payment;
}