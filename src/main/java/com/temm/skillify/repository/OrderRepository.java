package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Order;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
    List<Order> findBySalesPlan(SalesPlan salesPlan);
    List<Order> findByActiveTrue();
    List<Order> findByExpirationDateBefore(LocalDateTime date);
    Optional<Order> findByUserAndActiveTrue(User user);
}