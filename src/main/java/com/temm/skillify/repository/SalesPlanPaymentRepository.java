package com.temm.skillify.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.temm.skillify.model.entity.SalesPlanPayment;

@Repository
public interface SalesPlanPaymentRepository extends JpaRepository<SalesPlanPayment, String> {
    
}
