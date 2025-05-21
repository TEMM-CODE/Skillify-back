package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanMembershipEventReturnDTO;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SalesPlanMembershipEventMapper {

    public SalesPlanMembershipEventReturnDTO toDTO(SalesPlanMembershipEvent event) {
        if (event == null) {
            return null;
        }

        SalesPlanMembershipEventReturnDTO dto = new SalesPlanMembershipEventReturnDTO();
        dto.setId(event.getId() != null ? event.getId().toString() : null);
        dto.setCustomerId(event.getCustomer() != null && event.getCustomer().getId() != null ?
                event.getCustomer().getId().toString() : null);
        dto.setSalesPlanId(event.getSalesPlan() != null && event.getSalesPlan().getId() != null ?
                event.getSalesPlan().getId().toString() : null);
        dto.setStatus(event.getStatus());
        // Base fields mapping
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());

        return dto;
    }

    public SalesPlanMembershipEvent toEntity(SalesPlanMembershipEventCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        SalesPlanMembershipEvent entity = new SalesPlanMembershipEvent();
        entity.setStatus(dto.getStatus());
        // Customer and SalesPlan are set externally (e.g., by service layer)

        return entity;
    }

    public void updateEntityFromDTO(SalesPlanMembershipEventCreateDTO dto, SalesPlanMembershipEvent entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setStatus(dto.getStatus());
        // Customer and SalesPlan are not updated
    }
}