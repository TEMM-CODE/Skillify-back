package com.temm.skillify.service.mentor;


import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanAdminRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;
import com.temm.skillify.model.mapper.SalesPlanAdminMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanAdminMentorService {

    private final SalesPlanAdminRepository salesPlanAdminRepository;
    private final SalesPlanAdminMapper salesPlanAdminMapper;
    private final SalesPlanAdminMembershipEventRepository salesPlanAdminMembershipEventRepository;
    private final UserRepository userRepository;

     public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<SalesPlanAdminReturnDTO> getAllSalesPlans() {
        List<SalesPlanAdmin> salesPlans = salesPlanAdminRepository.findAll();
        return salesPlans.stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanAdminReturnDTO getSalesPlanById(String id) {
        SalesPlanAdmin salesPlan = salesPlanAdminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales plan not found with id: " + id));
        return salesPlanAdminMapper.toDTO(salesPlan);
    }

    public List<SalesPlanAdminReturnDTO> getSalesPlansByType(PlanType planType) {
        List<SalesPlanAdmin> salesPlans = salesPlanAdminRepository.findByType(planType);
        return salesPlans.stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<SalesPlanAdminReturnDTO> getUserSalesPlans(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        List<SalesPlanAdminMembershipEvent> membershipEvents = salesPlanAdminMembershipEventRepository.findByCustomer(user);
        return membershipEvents.stream()
                .map(SalesPlanAdminMembershipEvent::getSalesPlan)
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }
}