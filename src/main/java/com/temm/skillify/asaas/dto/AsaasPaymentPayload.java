package com.temm.skillify.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsaasPaymentPayload {
    private String object;
    private String id;
    private String customer;
    private String subscription;
    private String installment;
    private String paymentLink;
    private LocalDate dueDate;
    private LocalDate originalDueDate;
    private BigDecimal value;
    private BigDecimal netValue;
    private BigDecimal originalValue;
    private BigDecimal interestValue;
    private String nossoNumero;
    private String description;
    private String externalReference;
    private String billingType;
    private String status;
    private String pixTransaction;
    private LocalDate confirmedDate;
    private LocalDate paymentDate;
    private LocalDate clientPaymentDate;
    private Integer installmentNumber;
    private LocalDate creditDate;
    private String custody;
    private LocalDate estimatedCreditDate;
    private String invoiceUrl;
    private String bankSlipUrl;
    private String transactionReceiptUrl;
    private String invoiceNumber;
    private Boolean deleted;
    private Boolean anticipated;
    private Boolean anticipable;
    private String lastInvoiceViewedDate;
    private String lastBankSlipViewedDate;
    private Boolean postalService;

    private CreditCard creditCard;
    private Discount discount;
    private Fine fine;
    private Interest interest;
    private List<Split> split;
    private Chargeback chargeback;
    private List<Refund> refunds;
}