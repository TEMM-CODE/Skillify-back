package com.temm.skillify.service.unauthenticated;

import com.temm.skillify.model.dto.response.SalesPlanMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import com.temm.skillify.model.mapper.SalesPlanMapper;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;
import com.temm.skillify.repository.SalesPlanMembershipEventRepository;
import com.temm.skillify.repository.SalesPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanService {

    private final SalesPlanRepository salesPlanRepository;
    private final SalesPlanMapper salesPlanMapper;
    private final SalesPlanMembershipEventRepository membershipEventRepository;

    public List<SalesPlanResponseDTO> getAll() {
        return salesPlanRepository.findAll()
                .stream()
                .map(salesPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO getById(String id) {
        SalesPlan salesPlan = salesPlanRepository.findById(id)
                .orElse(null);
        return salesPlanMapper.toDTO(salesPlan);
    }

    public List<SalesPlanMembershipEventReturnDTO> getAllMembershipEvents() {
        return membershipEventRepository.findAll()
                .stream()
                .map(this::toMembershipEventDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanMembershipEventReturnDTO getMembershipEventById(String id) {
        SalesPlanMembershipEvent event = membershipEventRepository.findById(id)
                .orElse(null);
        return toMembershipEventDTO(event);
    }

    private SalesPlanMembershipEventReturnDTO toMembershipEventDTO(SalesPlanMembershipEvent event) {
        if (event == null) {
            return null;
        }
        SalesPlanMembershipEventReturnDTO dto = new SalesPlanMembershipEventReturnDTO();
        dto.setId(event.getId());
        dto.setCustomerId(event.getCustomer() != null ? event.getCustomer().getId() : null);
        dto.setSalesPlanId(event.getSalesPlan() != null ? event.getSalesPlan().getId() : null);
        dto.setStatus(event.getStatus());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        return dto;
    }
}