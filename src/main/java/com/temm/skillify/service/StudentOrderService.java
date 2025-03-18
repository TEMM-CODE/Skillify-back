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
public class StudentOrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private SalesPlanRepository salesPlanRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderMapper orderMapper;
    
    public List<OrderResponseDTO> findMyOrders(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        
        List<Order> orders = orderRepository.findByUser(user.get());
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<OrderResponseDTO> findMyActiveOrder(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Order> activeOrder = orderRepository.findByUserAndActiveTrue(user.get());
        return activeOrder.map(orderMapper::toDTO);
    }
    
    public OrderResponseDTO createOrder(OrderRequestDTO OrderRequestDTO) {
        User user = userRepository.findById(OrderRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        SalesPlan plan = salesPlanRepository.findById(OrderRequestDTO.getPlanId())
                .orElseThrow(() -> new RuntimeException("Sales plan not found"));
                
        LocalDateTime now = LocalDateTime.now();
        
        // Calculate expiration date based on plan type
        LocalDateTime expirationDate;
        if (plan.getType().name().equals("MENSAL")) {
            expirationDate = now.plusMonths(1);
        } else if (plan.getType().name().equals("ANUAL")) {
            expirationDate = now.plusYears(1);
        } else {
            expirationDate = now.plusMonths(1); // Default to 1 month
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setSalesPlan(plan);
        order.setPurchaseDate(now);
        order.setExpirationDate(expirationDate);
        order.setActive(true);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }
    
    public boolean hasActiveSubscription(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }
        
        Optional<Order> activeOrder = orderRepository.findByUserAndActiveTrue(user.get());
        return activeOrder.isPresent() && activeOrder.get().getExpirationDate().isAfter(LocalDateTime.now());
    }
    
    public Optional<OrderResponseDTO> getCurrentOrderDetails(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Order> activeOrder = orderRepository.findByUserAndActiveTrue(user.get());
        if (activeOrder.isPresent() && activeOrder.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            // Order is expired but still marked as active
            Order order = activeOrder.get();
            order.setActive(false);
            orderRepository.save(order);
            return Optional.empty();
        }
        
        return activeOrder.map(orderMapper::toDTO);
    }
}