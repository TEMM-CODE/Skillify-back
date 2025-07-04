package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.mentor.SalesPlanAdminMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/sales-plans-admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class SalesPlanAdminMentorController {
    private final SalesPlanAdminMentorService salesPlanAdminMentorService;

    @GetMapping
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getAllSalesPlans() {
        return ResponseEntity.ok(salesPlanAdminMentorService.getAllSalesPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanAdminReturnDTO> getSalesPlanById(@PathVariable String id) {
        return ResponseEntity.ok(salesPlanAdminMentorService.getSalesPlanById(id));
    }

    @GetMapping("/type/{planType}")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getSalesPlansByType(@PathVariable PlanType planType) {
        return ResponseEntity.ok(salesPlanAdminMentorService.getSalesPlansByType(planType));
    }

    @GetMapping("/my-plans")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getUserSalesPlans(Authentication authentication) {
        return ResponseEntity.ok(salesPlanAdminMentorService.getUserSalesPlans(authentication));
    }
}