package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;

import java.util.List;

@Repository
public interface SalesPlanRepository extends JpaRepository<SalesPlan, String> {
    List<SalesPlan> findByType(PlanType type);
}
