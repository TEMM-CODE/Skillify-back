package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanStudentService {

    private final SalesPlanRepository salesPlanRepository;

    public List<SalesPlanResponseDTO> getAllSalesPlans() {
        List<SalesPlan> salesPlans = salesPlanRepository.findAll();
        return salesPlans.stream()
                .map(this::mapToSalesPlanResponseDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO getSalesPlanById(String id) {
        SalesPlan salesPlan = salesPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found with id: " + id));
        return mapToSalesPlanResponseDTO(salesPlan);
    }

    public List<SalesPlanResponseDTO> getSalesPlansByType(PlanType planType) {
        List<SalesPlan> salesPlans = salesPlanRepository.findByType(planType);
        return salesPlans.stream()
                .map(this::mapToSalesPlanResponseDTO)
                .collect(Collectors.toList());
    }

    private SalesPlanResponseDTO mapToSalesPlanResponseDTO(SalesPlan salesPlan) {
        SalesPlanResponseDTO dto = new SalesPlanResponseDTO();
        dto.setId(salesPlan.getId());
        dto.setName(salesPlan.getName());
        dto.setDescription(salesPlan.getDescription());
        dto.setPrice(salesPlan.getPrice());
        dto.setType(salesPlan.getType());
        dto.setResources(salesPlan.getResources());
        dto.setCreatedAt(salesPlan.getCreatedAt());
        dto.setUpdatedAt(salesPlan.getUpdatedAt());
        return dto;
    }
}