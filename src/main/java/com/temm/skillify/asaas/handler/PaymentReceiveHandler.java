package com.temm.skillify.asaas.handler;

import org.springframework.stereotype.Service;

import com.temm.skillify.asaas.AsaasEventHandler;
import com.temm.skillify.asaas.EventHandler;
import com.temm.skillify.asaas.dto.AsaasWebhookPayload;
import com.temm.skillify.service.student.SalesPlanAdminPaymentService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.temm.skillify.model.enums.AsaasEvent;


@Service
@RequiredArgsConstructor
@EventHandler(AsaasEvent.PAYMENT_RECEIVED)
class SalesPlanAdminPaymentReceivedHandler implements AsaasEventHandler {

    private final SalesPlanAdminPaymentService salesPlanAdminPaymentService;


    @Override
    public void handle(AsaasWebhookPayload payload) {
        String paymentId = payload.getPayment().getId();
        String externalRef = payload.getPayment().getExternalReference(); // your order id
        //orderService.markAsPaid(externalRef, paymentId);
    }
}