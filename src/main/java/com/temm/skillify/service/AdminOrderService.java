package com.temm.skillify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.Order;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.dto.request.OrderRequestDTO;
import com.temm.skillify.model.dto.response.OrderResponseDTO;
import com.temm.skillify.repository.OrderRepository;
import com.temm.skillify.repository.SalesPlanRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.model.mapper.OrderMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminOrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SalesPlanRepository salesPlanRepository;
    
    @Autowired
    private OrderMapper orderMapper;
    
    public List<OrderResponseDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<OrderResponseDTO> findById(String id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::toDTO);
    }
    
    public List<OrderResponseDTO> findByUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        
        List<Order> orders = orderRepository.findByUser(user.get());
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponseDTO> findBySalesPlan(String planId) {
        Optional<SalesPlan> plan = salesPlanRepository.findById(planId);
        if (plan.isEmpty()) {
            return List.of();
        }
        
        return null;
    }
    
    public List<OrderResponseDTO> findActiveOrders() {
        List<Order> activeOrders = orderRepository.findByActiveTrue();
        return activeOrders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponseDTO> findExpiredOrders() {
        List<Order> expiredOrders = orderRepository.findByExpirationDateBefore(LocalDateTime.now());
        return expiredOrders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public OrderResponseDTO createOrder(OrderRequestDTO OrderRequestDTO) {
        User user = userRepository.findById(OrderRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        SalesPlan plan = salesPlanRepository.findById(OrderRequestDTO.getPlanId())
                .orElseThrow(() -> new RuntimeException("Sales plan not found"));
                
        LocalDateTime now = LocalDateTime.now();
        
        // Use months from DTO if provided, otherwise default to 1
        int months = OrderRequestDTO.getMonths() != null ? OrderRequestDTO.getMonths() : 1;
        LocalDateTime expirationDate = now.plusMonths(months);
        
        Order order = new Order();
        order.setUser(user);
        order.setPurchaseDate(now);
        order.setExpirationDate(expirationDate);
        order.setActive(true);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }
    
    public OrderResponseDTO updateOrder(String id, OrderResponseDTO orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (orderDetails.getSalesPlanId() != null) {
            SalesPlan salesPlan = salesPlanRepository.findById(orderDetails.getSalesPlanId())
                    .orElseThrow(() -> new RuntimeException("Sales plan not found"));
        }
        
        if (orderDetails.getUserId() != null) {
            User user = userRepository.findById(orderDetails.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
        
        if (orderDetails.getPurchaseDate() != null) {
            order.setPurchaseDate(orderDetails.getPurchaseDate());
        }
        
        if (orderDetails.getExpirationDate() != null) {
            order.setExpirationDate(orderDetails.getExpirationDate());
        }
        
        order.setActive(orderDetails.isActive());
        
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }
    
    public void deactivateExpiredOrders() {
        List<Order> expiredOrders = orderRepository.findByExpirationDateBefore(LocalDateTime.now());
        expiredOrders.forEach(order -> {
            order.setActive(false);
            orderRepository.save(order);
        });
    }
    
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}