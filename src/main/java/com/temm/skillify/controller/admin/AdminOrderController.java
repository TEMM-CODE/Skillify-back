package com.temm.skillify.controller.admin;



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
import com.temm.skillify.service.AdminOrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@SecurityRequirement(name = "ROLE_ADMIN")
@Tag(name = "Admin Order API", description = "Endpoints for administrative order management")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {
    @Autowired
    private AdminOrderService adminOrderService;
    
    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = adminOrderService.findAll();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        Optional<OrderResponseDTO> order = adminOrderService.findById(id);
        return order
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable String userId) {
        List<OrderResponseDTO> orders = adminOrderService.findByUser(userId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/plan/{planId}")
    @Operation(summary = "Get orders by sales plan ID")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersBySalesPlan(@PathVariable String planId) {
        List<OrderResponseDTO> orders = adminOrderService.findBySalesPlan(planId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active orders")
    public ResponseEntity<List<OrderResponseDTO>> getActiveOrders() {
        List<OrderResponseDTO> activeOrders = adminOrderService.findActiveOrders();
        return ResponseEntity.ok(activeOrders);
    }
    
    @GetMapping("/expired")
    @Operation(summary = "Get all expired orders")
    public ResponseEntity<List<OrderResponseDTO>> getExpiredOrders() {
        List<OrderResponseDTO> expiredOrders = adminOrderService.findExpiredOrders();
        return ResponseEntity.ok(expiredOrders);
    }
    
    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO createdOrder = adminOrderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing order")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable String id, 
            @RequestBody OrderResponseDTO orderDetails) {
        OrderResponseDTO updatedOrder = adminOrderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PostMapping("/deactivate-expired")
    @Operation(summary = "Deactivate all expired orders")
    public ResponseEntity<Void> deactivateExpiredOrders() {
        adminOrderService.deactivateExpiredOrders();
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        adminOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}