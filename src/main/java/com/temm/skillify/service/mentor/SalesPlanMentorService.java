package com.temm.skillify.service.mentor;

import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.model.enums.SalesPlanMembershipType;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.repository.SalesPlanRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.repository.SalesPlanMembershipEventRepository;
import com.temm.skillify.model.mapper.SalesPlanMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanMentorService {

    private final SalesPlanRepository salesPlanRepository;
    private final SalesPlanMapper salesPlanMapper;
    private final SalesPlanMembershipEventRepository salesPlanMembershipEventRepository;
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private void validateMentorRole(User user) {
        if (user.getRole() != UserRole.MENTOR) {
            throw new SecurityException("User does not have MENTOR role");
        }
    }

    public List<SalesPlanResponseDTO> getAllSalesPlans(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        List<SalesPlan> salesPlans = salesPlanRepository.findAll();
        return salesPlans.stream()
                .map(salesPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO getSalesPlanById(Authentication authentication, String id) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlan salesPlan = salesPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found with id: " + id));
        return salesPlanMapper.toDTO(salesPlan);
    }

    public List<SalesPlanResponseDTO> getSalesPlansByType(Authentication authentication, PlanType planType) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        List<SalesPlan> salesPlans = salesPlanRepository.findByType(planType);
        return salesPlans.stream()
                .map(salesPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanResponseDTO createSalesPlan(Authentication authentication, SalesPlanCreateDTO createDTO) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlan salesPlan = salesPlanMapper.toEntity(createDTO);
        salesPlan.setUsers(List.of(user)); // Associate the mentor as a user
        SalesPlan savedSalesPlan = salesPlanRepository.save(salesPlan);
        return salesPlanMapper.toDTO(savedSalesPlan);
    }

    public SalesPlanResponseDTO updateSalesPlan(Authentication authentication, String id, SalesPlanCreateDTO updateDTO) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlan salesPlan = salesPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found with id: " + id));
        salesPlanMapper.updateEntityFromDTO(updateDTO, salesPlan);
        SalesPlan updatedSalesPlan = salesPlanRepository.save(salesPlan);
        return salesPlanMapper.toDTO(updatedSalesPlan);
    }

    public void deleteSalesPlan(Authentication authentication, String id) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlan salesPlan = salesPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found with id: " + id));
        salesPlanRepository.delete(salesPlan);
    }

    public SalesPlanMembershipEvent getSalesPlanMembershipEvent(Authentication authentication, String eventId) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        return salesPlanMembershipEventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Membership event not found with id: " + eventId));
    }

    public void deleteSalesPlanMembershipEvent(Authentication authentication, String eventId) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlanMembershipEvent event = salesPlanMembershipEventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Membership event not found with id: " + eventId));
        salesPlanMembershipEventRepository.delete(event);
    }

    public SalesPlanMembershipEvent updateSalesPlanMembershipEvent(Authentication authentication, String eventId, SalesPlanMembershipType newStatus) {
        User user = getUserFromAuthentication(authentication);
        validateMentorRole(user);
        SalesPlanMembershipEvent event = salesPlanMembershipEventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Membership event not found with id: " + eventId));
        event.setStatus(newStatus);
        return salesPlanMembershipEventRepository.save(event);
    }
}