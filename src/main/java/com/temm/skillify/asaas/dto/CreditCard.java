package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditCard {
    private String creditCardNumber;     // last 4 digits
    private String creditCardBrand;
    private String creditCardToken;
}