package com.temm.skillify.service.admin;

import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.dto.request.SalesPlanMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.SalesPlanMapper;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;
import com.temm.skillify.repository.SalesPlanMembershipEventRepository;
import com.temm.skillify.repository.SalesPlanRepository;
import com.temm.skillify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSalesPlanService {

    private final SalesPlanRepository salesPlanRepository;
    private final SalesPlanMapper salesPlanMapper;
    private final UserRepository userRepository;
    private final SalesPlanMembershipEventRepository membershipEventRepository;

    // SalesPlan methods
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

    public SalesPlanResponseDTO create(SalesPlanCreateDTO createDTO) {
        SalesPlan entity = salesPlanMapper.toEntity(createDTO);
        SalesPlan savedEntity = salesPlanRepository.save(entity);
        return salesPlanMapper.toDTO(savedEntity);
    }

    public SalesPlanResponseDTO edit(String id, SalesPlanCreateDTO updateDTO) {
        SalesPlan entity = salesPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + id));
        
        salesPlanMapper.updateEntityFromDTO(updateDTO, entity);
        SalesPlan updatedEntity = salesPlanRepository.save(entity);
        return salesPlanMapper.toDTO(updatedEntity);
    }

    public void delete(String id) {
        if (!salesPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Sales plan not found with ID: " + id);
        }
        salesPlanRepository.deleteById(id);
    }

    // SalesPlanMembershipEvent methods
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

    public SalesPlanMembershipEventReturnDTO createMembershipEvent(SalesPlanMembershipEventCreateDTO createDTO) {
        User customer = userRepository.findById(createDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + createDTO.getCustomerId()));
        SalesPlan salesPlan = salesPlanRepository.findById(createDTO.getSalesPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + createDTO.getSalesPlanId()));

        SalesPlanMembershipEvent event = new SalesPlanMembershipEvent();
        event.setCustomer(customer);
        event.setSalesPlan(salesPlan);
        event.setStatus(createDTO.getStatus());

        SalesPlanMembershipEvent savedEvent = membershipEventRepository.save(event);
        return toMembershipEventDTO(savedEvent);
    }

    public SalesPlanMembershipEventReturnDTO updateMembershipEvent(String id, SalesPlanMembershipEventCreateDTO updateDTO) {
        SalesPlanMembershipEvent event = membershipEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membership event not found with ID: " + id));

        User customer = userRepository.findById(updateDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + updateDTO.getCustomerId()));
        SalesPlan salesPlan = salesPlanRepository.findById(updateDTO.getSalesPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + updateDTO.getSalesPlanId()));

        event.setCustomer(customer);
        event.setSalesPlan(salesPlan);
        event.setStatus(updateDTO.getStatus());

        SalesPlanMembershipEvent updatedEvent = membershipEventRepository.save(event);
        return toMembershipEventDTO(updatedEvent);
    }

    public void deleteMembershipEvent(String id) {
        if (!membershipEventRepository.existsById(id)) {
            throw new IllegalArgumentException("Membership event not found with ID: " + id);
        }
        membershipEventRepository.deleteById(id);
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