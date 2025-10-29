package com.temm.skillify.asaas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temm.skillify.asaas.AsaasEventHandler;
import com.temm.skillify.asaas.AsaasEventHandlerRegistry;
import com.temm.skillify.asaas.dto.AsaasWebhookPayload;
import com.temm.skillify.asaas.security.AsaasSignatureVerifier;
import com.temm.skillify.model.entity.ProcessedAsaasEvent;
import com.temm.skillify.model.enums.AsaasEvent;
import com.temm.skillify.repository.ProcessedEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AsaasWebhookController {

    private final AsaasSignatureVerifier verifier;
    private final ProcessedEventRepository processedRepo;
    private final AsaasEventHandlerRegistry registry;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @PostMapping(value = "/webhooks/asaas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> receive(@RequestBody String rawPayload,
                                        @RequestHeader(value = "X-Asaas-Signature", required = false) String signature) {

        // 1. Verify signature
        if (!verifier.isValid(rawPayload, signature)) {
            log.warn("Invalid Asaas signature");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Deserialize (ignore unknown fields)
        AsaasWebhookPayload payload;
        try {
            payload = mapper.readValue(rawPayload, AsaasWebhookPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse Asaas webhook", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 3. Idempotency
        String eventId = payload.getId();
        if (processedRepo.existsByEventId(eventId)) {
            log.info("Event {} already processed", eventId);
            return ResponseEntity.ok().build(); // 200 = acknowledged
        }

        // 4. Dispatch to handler (async)
        AsaasEvent event = AsaasEvent.fromString(payload.getEvent());
        registry.get(event).ifPresentOrElse(
                handler -> processAsync(eventId, payload, handler),
                () -> log.info("No handler for event {}", event)
        );

        // 5. Immediate ACK (Asaas expects 2xx within 10 s)
        return ResponseEntity.ok().build();
    }

    @Async
    void processAsync(String eventId, AsaasWebhookPayload payload, AsaasEventHandler handler) {
        try {
            handler.handle(payload);
            // persist only after success
            ProcessedAsaasEvent event = new ProcessedAsaasEvent();
            event.setEventId(eventId);
            processedRepo.save(event);
        } catch (Exception ex) {
            log.error("Handler failed for event {}", eventId, ex);
            // optional: push to DLQ (Dead-Letter Queue)
        }
    }
}