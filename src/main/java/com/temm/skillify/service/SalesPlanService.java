package com.temm.skillify.service;


import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesPlanService {

    private final SalesPlanRepository salesPlanRepository;

    public List<SalesPlan> findAll() {
        return salesPlanRepository.findAll();
    }

    public SalesPlan findById(String id) {
        return salesPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found"));
    }

    public List<SalesPlan> findByType(PlanType type) {
        return salesPlanRepository.findByType(type);
    }
}