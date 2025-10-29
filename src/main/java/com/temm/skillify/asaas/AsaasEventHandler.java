package com.temm.skillify.asaas;

import com.temm.skillify.asaas.dto.AsaasWebhookPayload;

public interface AsaasEventHandler {
    void handle(AsaasWebhookPayload payload);
}