package com.temm.skillify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.SalesPlanAdminPayment;

@Repository
public interface SalesPlanAdminPaymentRepository extends JpaRepository<SalesPlanAdminPayment, String> {
    
}
