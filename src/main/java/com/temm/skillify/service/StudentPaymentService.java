package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.PaymentRequestDTO;
import com.temm.skillify.model.dto.response.PaymentResponseDTO;
import com.temm.skillify.model.entity.Order;
import com.temm.skillify.model.entity.Payment;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PaymentStatus;
import com.temm.skillify.model.mapper.PaymentMapper;
import com.temm.skillify.repository.OrderRepository;
import com.temm.skillify.repository.PaymentRepository;
import com.temm.skillify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentMapper paymentMapper;
    
    // Get a student's payments
    public List<PaymentResponseDTO> getMyPayments(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        List<Payment> payments = paymentRepository.findByUser(user);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get a student's payment by ID
    public PaymentResponseDTO getPaymentById(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));
        
        // Ensure the payment belongs to the requesting user
        if (!payment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Payment does not belong to the authenticated user");
        }
        
        return paymentMapper.toDTO(payment);
    }
    
    // Get payments by status for a student
    public List<PaymentResponseDTO> getMyPaymentsByStatus(String userId, PaymentStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        List<Payment> payments = paymentRepository.findByUserAndStatus(user, status);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Create a new payment (e.g., when purchasing a course)
    @Transactional
    public PaymentResponseDTO makePayment(PaymentRequestDTO requestDTO, String userId) {
        // Set the authenticated user's ID in the request
        requestDTO.setUserId(userId);
        
        // Ensure the payment date is set
        if (requestDTO.getPaymentDate() == null) {
            requestDTO.setPaymentDate(LocalDateTime.now());
        }
        
        // Initialize with PENDING status if not specified
        if (requestDTO.getStatus() == null) {
            requestDTO.setStatus(PaymentStatus.PENDING);
        }
        
        // Create and save the payment
        Payment payment = paymentMapper.toEntity(requestDTO);
        payment = paymentRepository.save(payment);
        
        return paymentMapper.toDTO(payment);
    }
    
    // Verify payment receipt (for example, after returning from a payment gateway)
    @Transactional
    public PaymentResponseDTO verifyPayment(String paymentId, String transactionId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));
        
        // Ensure the payment belongs to the requesting user
        if (!payment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Payment does not belong to the authenticated user");
        }
        
        // Update transaction details
        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.PENDING);
        
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }
    
    // Cancel a pending payment
    @Transactional
    public PaymentResponseDTO cancelPayment(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));
        
        // Ensure the payment belongs to the requesting user
        if (!payment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Payment does not belong to the authenticated user");
        }
        
        // Only allow cancellation of pending payments
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only pending payments can be cancelled");
        }
        
        payment.setStatus(PaymentStatus.CANCELLED);
        payment = paymentRepository.save(payment);
        
        return paymentMapper.toDTO(payment);
    }
    
    // Get payments for a specific order
    public List<PaymentResponseDTO> getPaymentsByOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        // Ensure the order belongs to the requesting user
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to the authenticated user");
        }
        
        List<Payment> payments = paymentRepository.findByOrder(order);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
}