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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentMapper paymentMapper;
    
    // Get all payments with pagination
    public List<PaymentResponseDTO> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get payment by ID
    public PaymentResponseDTO getPaymentById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
        return paymentMapper.toDTO(payment);
    }
    
    // Get payments by user
    public List<PaymentResponseDTO> getPaymentsByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        List<Payment> payments = paymentRepository.findByUser(user);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get payments by order
    public List<PaymentResponseDTO> getPaymentsByOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        List<Payment> payments = paymentRepository.findByOrder(order);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get payments by status
    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get payments by date range
    public List<PaymentResponseDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Create a new payment
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {
        Payment payment = paymentMapper.toEntity(requestDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }
    
    // Update payment status
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(String id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
        
        payment.setStatus(status);
        
        if (status == PaymentStatus.COMPLETED) {
            payment.setProcessingDate(LocalDateTime.now());
        }
        
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }
    
    // Update payment details
    @Transactional
    public PaymentResponseDTO updatePayment(String id, PaymentRequestDTO requestDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
        
        if (requestDTO.getAmount() != null) {
            payment.setAmount(requestDTO.getAmount());
        }
        
        if (requestDTO.getOrderId() != null) {
            Order order = orderRepository.findById(requestDTO.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + requestDTO.getOrderId()));
            payment.setOrder(order);
        }
        
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + requestDTO.getUserId()));
            payment.setUser(user);
        }
        
        if (requestDTO.getStatus() != null) {
            payment.setStatus(requestDTO.getStatus());
            
            if (requestDTO.getStatus() == PaymentStatus.COMPLETED) {
                payment.setProcessingDate(LocalDateTime.now());
            }
        }
        
        if (requestDTO.getPaymentMethod() != null) {
            payment.setPaymentMethod(requestDTO.getPaymentMethod());
        }
        
        if (requestDTO.getTransactionId() != null) {
            payment.setTransactionId(requestDTO.getTransactionId());
        }
        
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }
    
    // Delete payment
    @Transactional
    public void deletePayment(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }
}