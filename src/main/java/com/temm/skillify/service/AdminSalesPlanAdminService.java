package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.SalesPlanAdminCreateDTO;
import com.temm.skillify.model.dto.request.SalesPlanAdminMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.SalesPlanAdminMapper;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;
import com.temm.skillify.repository.SalesPlanAdminRepository;
import com.temm.skillify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSalesPlanAdminService {

    private final SalesPlanAdminRepository salesPlanAdminRepository;
    private final SalesPlanAdminMapper salesPlanAdminMapper;
    private final UserRepository userRepository;
    private final SalesPlanAdminMembershipEventRepository membershipEventRepository;

    // SalesPlanAdmin methods
    public List<SalesPlanAdminReturnDTO> getAll() {
        return salesPlanAdminRepository.findAll()
                .stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanAdminReturnDTO getById(String id) {
        SalesPlanAdmin salesPlanAdmin = salesPlanAdminRepository.findById(id)
                .orElse(null);
        return salesPlanAdminMapper.toDTO(salesPlanAdmin);
    }

    public List<SalesPlanAdminReturnDTO> getByCreator(String creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + creatorId));
        return salesPlanAdminRepository.findByCreator(creator)
                .stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanAdminReturnDTO create(SalesPlanAdminCreateDTO createDTO, String creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + creatorId));
        
        SalesPlanAdmin entity = salesPlanAdminMapper.toEntity(createDTO);
        entity.setCreator(creator);
        SalesPlanAdmin savedEntity = salesPlanAdminRepository.save(entity);
        return salesPlanAdminMapper.toDTO(savedEntity);
    }

    public SalesPlanAdminReturnDTO edit(String id, SalesPlanAdminCreateDTO updateDTO) {
        SalesPlanAdmin entity = salesPlanAdminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + id));
        
        salesPlanAdminMapper.updateEntityFromDTO(updateDTO, entity);
        SalesPlanAdmin updatedEntity = salesPlanAdminRepository.save(entity);
        return salesPlanAdminMapper.toDTO(updatedEntity);
    }

    public void delete(String id) {
        if (!salesPlanAdminRepository.existsById(id)) {
            throw new IllegalArgumentException("Sales plan not found with ID: " + id);
        }
        salesPlanAdminRepository.deleteById(id);
    }

    // SalesPlanAdminMembershipEvent methods
    public List<SalesPlanAdminMembershipEventReturnDTO> getAllMembershipEvents() {
        return membershipEventRepository.findAll()
                .stream()
                .map(this::toMembershipEventDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanAdminMembershipEventReturnDTO getMembershipEventById(String id) {
        SalesPlanAdminMembershipEvent event = membershipEventRepository.findById(id)
                .orElse(null);
        return toMembershipEventDTO(event);
    }

    public SalesPlanAdminMembershipEventReturnDTO createMembershipEvent(SalesPlanAdminMembershipEventCreateDTO createDTO) {
        User customer = userRepository.findById(createDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + createDTO.getCustomerId()));
        SalesPlanAdmin salesPlan = salesPlanAdminRepository.findById(createDTO.getSalesPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + createDTO.getSalesPlanId()));

        SalesPlanAdminMembershipEvent event = new SalesPlanAdminMembershipEvent();
        event.setCustomer(customer);
        event.setSalesPlan(salesPlan);
        event.setStatus(createDTO.getStatus());

        SalesPlanAdminMembershipEvent savedEvent = membershipEventRepository.save(event);
        return toMembershipEventDTO(savedEvent);
    }

    public SalesPlanAdminMembershipEventReturnDTO updateMembershipEvent(String id, SalesPlanAdminMembershipEventCreateDTO updateDTO) {
        SalesPlanAdminMembershipEvent event = membershipEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membership event not found with ID: " + id));

        User customer = userRepository.findById(updateDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + updateDTO.getCustomerId()));
        SalesPlanAdmin salesPlan = salesPlanAdminRepository.findById(updateDTO.getSalesPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Sales plan not found with ID: " + updateDTO.getSalesPlanId()));

        event.setCustomer(customer);
        event.setSalesPlan(salesPlan);
        event.setStatus(updateDTO.getStatus());

        SalesPlanAdminMembershipEvent updatedEvent = membershipEventRepository.save(event);
        return toMembershipEventDTO(updatedEvent);
    }

    public void deleteMembershipEvent(String id) {
        if (!membershipEventRepository.existsById(id)) {
            throw new IllegalArgumentException("Membership event not found with ID: " + id);
        }
        membershipEventRepository.deleteById(id);
    }

    private SalesPlanAdminMembershipEventReturnDTO toMembershipEventDTO(SalesPlanAdminMembershipEvent event) {
        if (event == null) {
            return null;
        }
        SalesPlanAdminMembershipEventReturnDTO dto = new SalesPlanAdminMembershipEventReturnDTO();
        dto.setId(event.getId());
        dto.setCustomerId(event.getCustomer() != null ? event.getCustomer().getId() : null);
        dto.setSalesPlanId(event.getSalesPlan() != null ? event.getSalesPlan().getId() : null);
        dto.setStatus(event.getStatus());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        return dto;
    }
}