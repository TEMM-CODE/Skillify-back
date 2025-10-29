package com.temm.skillify.asaas.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class AsaasSignatureVerifier {

    @Value("${asaas.webhook.secret}")
    private String secret;

    private Mac mac;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, InvalidKeyException {
        mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    }

    /** Asaas sends header: X-Asaas-Signature = base64(hmac(payload)) */
    public boolean isValid(String payload, String signatureHeader) {
        if (signatureHeader == null || payload == null) return false;
        byte[] expected = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String computed = Base64.getEncoder().encodeToString(expected);
        return computed.equals(signatureHeader);
    }
}