package com.temm.skillify.asaas.handler;

import org.springframework.stereotype.Service;

import com.temm.skillify.asaas.AsaasEventHandler;
import com.temm.skillify.asaas.EventHandler;
import com.temm.skillify.asaas.dto.AsaasWebhookPayload;
import com.temm.skillify.service.student.SalesPlanAdminPaymentService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.temm.skillify.model.entity.SalesPlanAdminPayment;
import com.temm.skillify.model.enums.AsaasEvent;
import com.temm.skillify.repository.SalesPlanAdminPaymentRepository;


@Service
@RequiredArgsConstructor
@EventHandler(AsaasEvent.PAYMENT_DELETED)
class PaymentDeletedHandler implements AsaasEventHandler {

    private final SalesPlanAdminPaymentRepository salesPlanAdminPaymentRepository;


    @Override
    public void handle(AsaasWebhookPayload payload) {
        String paymentLink = payload.getPayment().getPaymentLink();
        String externalRef = payload.getPayment().getExternalReference(); // your order id
        //usar external ou payment link pra chamar o service find
        SalesPlanAdminPayment payment = salesPlanAdminPaymentRepository.findByPaymentLink(paymentLink).get(0);
        payment.setStatus(AsaasEvent.PAYMENT_DELETED);
        salesPlanAdminPaymentRepository.save(payment);

    }
}