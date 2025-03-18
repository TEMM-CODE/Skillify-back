package com.temm.skillify.controller.student;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.temm.skillify.model.dto.request.OrderRequestDTO;
import com.temm.skillify.model.dto.response.OrderResponseDTO;
import com.temm.skillify.service.StudentOrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student/orders")
@SecurityRequirement(name = "ROLE_ESTUDANTE")
@Tag(name = "Student Order API", description = "Endpoints for student orders management")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class StudentOrderController {
    @Autowired
    private StudentOrderService studentOrderService;
    
    @GetMapping("/my")
    @Operation(summary = "Get all orders for the authenticated student")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@RequestParam String userId) {
        List<OrderResponseDTO> orders = studentOrderService.findMyOrders(userId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/my/active")
    @Operation(summary = "Get active order for the authenticated student")
    public ResponseEntity<OrderResponseDTO> getMyActiveOrder(@RequestParam String userId) {
        Optional<OrderResponseDTO> activeOrder = studentOrderService.findMyActiveOrder(userId);
        return activeOrder
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO createdOrder = studentOrderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    
    @GetMapping("/subscription/status")
    @Operation(summary = "Check if student has active subscription")
    public ResponseEntity<Boolean> checkSubscriptionStatus(@RequestParam String userId) {
        boolean hasActiveSubscription = studentOrderService.hasActiveSubscription(userId);
        return ResponseEntity.ok(hasActiveSubscription);
    }
    
    @GetMapping("/current")
    @Operation(summary = "Get current order details")
    public ResponseEntity<OrderResponseDTO> getCurrentOrderDetails(@RequestParam String userId) {
        Optional<OrderResponseDTO> currentOrder = studentOrderService.getCurrentOrderDetails(userId);
        return currentOrder
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}