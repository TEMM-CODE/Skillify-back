package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanAdminCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalesPlanAdminMapper {

    private final SalesPlanAdminMembershipEventMapper membershipEventMapper;

    private final SalesPlanAdminMembershipEventRepository membershipEventRepository;


 
    public SalesPlanAdminReturnDTO toDTO(SalesPlanAdmin salesPlanAdmin) {
        if (salesPlanAdmin == null) {
            return null;
        }

        SalesPlanAdminReturnDTO dto = new SalesPlanAdminReturnDTO();
        dto.setId(salesPlanAdmin.getId() != null ? salesPlanAdmin.getId().toString() : null);
        dto.setName(salesPlanAdmin.getName());
        dto.setDescription(salesPlanAdmin.getDescription());
        dto.setPrice(salesPlanAdmin.getPrice());
        dto.setType(salesPlanAdmin.getType());
        dto.setResources(salesPlanAdmin.getResources());
        // Fetch membership events using repository
        // Base fields mapping
        dto.setCreatedAt(salesPlanAdmin.getCreatedAt());
        dto.setUpdatedAt(salesPlanAdmin.getUpdatedAt());

        return dto;
    }

    public SalesPlanAdmin toEntity(SalesPlanAdminCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        SalesPlanAdmin entity = new SalesPlanAdmin();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
        // Creator is set externally (e.g., by service layer)
        // MembershipEvents are not set in create

        return entity;
    }

    public void updateEntityFromDTO(SalesPlanAdminCreateDTO dto, SalesPlanAdmin entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
        // Creator and MembershipEvents are not updated
    }
}