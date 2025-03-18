package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Order;
import com.temm.skillify.model.entity.Payment;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByUser(User user);
    List<Payment> findByOrder(Order order);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Payment> findByUserAndStatus(User user, PaymentStatus status);
}