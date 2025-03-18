package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.request.PaymentRequestDTO;
import com.temm.skillify.model.dto.response.PaymentResponseDTO;
import com.temm.skillify.model.enums.PaymentStatus;
import com.temm.skillify.service.StudentPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Student Payment Controller", description = "APIs for managing student payments")
@SecurityRequirement(name = "ROLE_ESTUDANTE")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class StudentPaymentController {

    @Autowired
    private StudentPaymentService studentPaymentService;

    @GetMapping("/my-payments")
    @Operation(summary = "Get current student's payments")
    public ResponseEntity<List<PaymentResponseDTO>> getMyPayments(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.getMyPayments(userId));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable String paymentId,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.getPaymentById(paymentId, userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    public ResponseEntity<List<PaymentResponseDTO>> getMyPaymentsByStatus(
            @PathVariable PaymentStatus status,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.getMyPaymentsByStatus(userId, status));
    }

    @PostMapping
    @Operation(summary = "Make a payment")
    public ResponseEntity<PaymentResponseDTO> makePayment(
            @Valid @RequestBody PaymentRequestDTO requestDTO,
            Authentication authentication) {
        String userId = authentication.getName();
        return new ResponseEntity<>(studentPaymentService.makePayment(requestDTO, userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{paymentId}/verify")
    @Operation(summary = "Verify payment receipt")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @PathVariable String paymentId,
            @RequestParam String transactionId,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.verifyPayment(paymentId, transactionId, userId));
    }

    @PatchMapping("/{paymentId}/cancel")
    @Operation(summary = "Cancel a pending payment")
    public ResponseEntity<PaymentResponseDTO> cancelPayment(
            @PathVariable String paymentId,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.cancelPayment(paymentId, userId));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payments for a specific order")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrder(
            @PathVariable String orderId,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(studentPaymentService.getPaymentsByOrder(orderId, userId));
    }
}