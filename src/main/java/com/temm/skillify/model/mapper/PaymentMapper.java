package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.request.PaymentRequestDTO;
import com.temm.skillify.model.dto.response.PaymentResponseDTO;
import com.temm.skillify.model.entity.Order;
import com.temm.skillify.model.entity.Payment;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.OrderRepository;
import com.temm.skillify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentMapper {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Payment toEntity(PaymentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        
        // Set order if orderId exists
        if (dto.getOrderId() != null) {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + dto.getOrderId()));
            payment.setOrder(order);
        }
        
        // Set user if userId exists
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId()));
            payment.setUser(user);
        }
        
        payment.setStatus(dto.getStatus());
        payment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionId(dto.getTransactionId());
        
        return payment;
    }
    
    public PaymentResponseDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        
        if (payment.getOrder() != null) {
            dto.setOrderId(payment.getOrder().getId());
            dto.setOrderDetails(payment.getOrder().getSalesPlan().getName() + " - " + 
                                payment.getOrder().getSalesPlan().getPrice());
        }
        
        if (payment.getUser() != null) {
            dto.setUserId(payment.getUser().getId());
            dto.setUserName(payment.getUser().getName());
        }
        
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setProcessingDate(payment.getProcessingDate());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        
        return dto;
    }
}