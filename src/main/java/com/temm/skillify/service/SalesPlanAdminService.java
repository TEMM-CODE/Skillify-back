package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanRepository;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.mapper.SalesPlanMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanAdminService {
    private final SalesPlanRepository salesPlanRepository;
    private final SalesPlanMapper salesPlanMapper;

    public List<SalesPlanResponseDTO> findAll() {
        return salesPlanRepository.findAll().stream()
            .map(salesPlanMapper::toDTO)
            .collect(Collectors.toList());
    }

    public Optional<SalesPlanResponseDTO> findById(String id) {
        return salesPlanRepository.findById(id)
            .map(salesPlanMapper::toDTO);
    }

    public List<SalesPlanResponseDTO> findByType(PlanType type) {
        return salesPlanRepository.findByType(type).stream()
            .map(salesPlanMapper::toDTO)
            .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO save(SalesPlanCreateDTO salesPlanDTO) {
        SalesPlan salesPlan = toEntity(salesPlanDTO);
        SalesPlan savedPlan = salesPlanRepository.save(salesPlan);
        return salesPlanMapper.toDTO(savedPlan);
    }

    public Optional<SalesPlanResponseDTO> update(String id, SalesPlanCreateDTO salesPlanDTO) {
        return salesPlanRepository.findById(id)
            .map(existingPlan -> {
                existingPlan.setName(salesPlanDTO.getName());
                existingPlan.setDescription(salesPlanDTO.getDescription());
                existingPlan.setPrice(salesPlanDTO.getPrice());
                existingPlan.setType(salesPlanDTO.getType());
                existingPlan.setResources(salesPlanDTO.getResources());
                return salesPlanRepository.save(existingPlan);
            })
            .map(salesPlanMapper::toDTO);
    }

    public void deleteById(String id) {
        salesPlanRepository.deleteById(id);
    }

    private SalesPlan toEntity(SalesPlanCreateDTO dto) {
        SalesPlan entity = new SalesPlan();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
        return entity;
    }
}