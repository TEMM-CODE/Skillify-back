package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.PaymentRequestDTO;
import com.temm.skillify.model.dto.response.PaymentResponseDTO;
import com.temm.skillify.model.enums.PaymentStatus;
import com.temm.skillify.service.AdminPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@Tag(name = "Admin Payment Controller", description = "APIs for managing payments by administrators")
@SecurityRequirement(name = "ROLE_ADMIN")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminPaymentController {

    @Autowired
    private AdminPaymentService adminPaymentService;

    @GetMapping
    @Operation(summary = "Get all payments with pagination")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments(Pageable pageable) {
        return ResponseEntity.ok(adminPaymentService.getAllPayments(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable String id) {
        return ResponseEntity.ok(adminPaymentService.getPaymentById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get payments by user ID")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminPaymentService.getPaymentsByUser(userId));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payments by order ID")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(adminPaymentService.getPaymentsByOrder(orderId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(adminPaymentService.getPaymentsByStatus(status));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get payments by date range")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(adminPaymentService.getPaymentsByDateRange(startDate, endDate));
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        return new ResponseEntity<>(adminPaymentService.createPayment(requestDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(adminPaymentService.updatePaymentStatus(id, status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment details")
    public ResponseEntity<PaymentResponseDTO> updatePayment(
            @PathVariable String id,
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        return ResponseEntity.ok(adminPaymentService.updatePayment(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        adminPaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}