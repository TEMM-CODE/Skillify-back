package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import org.springframework.stereotype.Component;

@Component
public class SalesPlanMapper {
    public SalesPlanResponseDTO toDTO(SalesPlan salesPlan) {
        if (salesPlan == null) {
            return null;
        }
        
        SalesPlanResponseDTO dto = new SalesPlanResponseDTO();
        dto.setId(salesPlan.getId());
        dto.setName(salesPlan.getName());
        dto.setDescription(salesPlan.getDescription());
        dto.setPrice(salesPlan.getPrice());
        dto.setType(salesPlan.getType());
        dto.setResources(salesPlan.getResources());
        // Base fields mapping
        dto.setCreatedAt(salesPlan.getCreatedAt());
        dto.setUpdatedAt(salesPlan.getUpdatedAt());
        
        return dto;
    }
    
    public SalesPlan toEntity(SalesPlanCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        SalesPlan entity = new SalesPlan();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
        
        return entity;
    }
    
    public void updateEntityFromDTO(SalesPlanCreateDTO dto, SalesPlan entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
    }
}