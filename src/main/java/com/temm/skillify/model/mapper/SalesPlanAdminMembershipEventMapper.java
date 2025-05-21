package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanAdminMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminMembershipEventReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SalesPlanAdminMembershipEventMapper {

    public SalesPlanAdminMembershipEventReturnDTO toDTO(SalesPlanAdminMembershipEvent event) {
        if (event == null) {
            return null;
        }

        SalesPlanAdminMembershipEventReturnDTO dto = new SalesPlanAdminMembershipEventReturnDTO();
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

    public SalesPlanAdminMembershipEvent toEntity(SalesPlanAdminMembershipEventCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        SalesPlanAdminMembershipEvent entity = new SalesPlanAdminMembershipEvent();
        entity.setStatus(dto.getStatus());
        // Customer and SalesPlan are set externally (e.g., by service layer)

        return entity;
    }

    public void updateEntityFromDTO(SalesPlanAdminMembershipEventCreateDTO dto, SalesPlanAdminMembershipEvent entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setStatus(dto.getStatus());
        // Customer and SalesPlan are not updated
    }
}