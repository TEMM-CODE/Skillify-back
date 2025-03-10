package com.temm.skillify.model.mapper;


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
        
        // If BaseResponseDTO has createdAt, updatedAt fields, map them too
        dto.setCreatedAt(salesPlan.getCreatedAt());
        dto.setUpdatedAt(salesPlan.getUpdatedAt());
        
        return dto;
    }
}