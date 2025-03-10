package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.model.mapper.SalesPlanMapper;
import com.temm.skillify.repository.SalesPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanService {
    private final SalesPlanRepository salesPlanRepository;
    private final SalesPlanMapper salesPlanMapper;

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

    // New DTO methods
    public List<SalesPlanResponseDTO> findAllDTOs() {
        return findAll().stream()
                .map(salesPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO findDTOById(String id) {
        return salesPlanMapper.toDTO(findById(id));
    }

    public List<SalesPlanResponseDTO> findDTOsByType(PlanType type) {
        return findByType(type).stream()
                .map(salesPlanMapper::toDTO)
                .collect(Collectors.toList());
    }
}