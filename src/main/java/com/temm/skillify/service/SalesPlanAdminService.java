package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesPlanAdminService {
    
    private final SalesPlanRepository salesPlanRepository;
    
    public List<SalesPlan> findAll() {
        return salesPlanRepository.findAll();
    }
    
    public Optional<SalesPlan> findById(String id) {
        return salesPlanRepository.findById(id);
    }
    
    public List<SalesPlan> findByType(PlanType type) {
        return salesPlanRepository.findByType(type);
    }
    
    public SalesPlan save(SalesPlan salesPlan) {
        return salesPlanRepository.save(salesPlan);
    }
    
    public void deleteById(String id) {
        salesPlanRepository.deleteById(id);
    }
}