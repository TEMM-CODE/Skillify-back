package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.student.SalesPlanAdminStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudante/planos-venda-admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class SalesPlanAdminStudentController {

    private final SalesPlanAdminStudentService salesPlanAdminStudentService;

    @GetMapping
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getAllSalesPlans() {
        List<SalesPlanAdminReturnDTO> salesPlans = salesPlanAdminStudentService.getAllSalesPlans();
        return ResponseEntity.ok(salesPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanAdminReturnDTO> getSalesPlanById(@PathVariable String id) {
        SalesPlanAdminReturnDTO salesPlan = salesPlanAdminStudentService.getSalesPlanById(id);
        return ResponseEntity.ok(salesPlan);
    }

    @GetMapping("/tipo/{planType}")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getSalesPlansByType(@PathVariable PlanType planType) {
        List<SalesPlanAdminReturnDTO> salesPlans = salesPlanAdminStudentService.getSalesPlansByType(planType);
        return ResponseEntity.ok(salesPlans);
    }

    @GetMapping("/meus-planos")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getUserSalesPlans(Authentication authentication) {
        List<SalesPlanAdminReturnDTO> salesPlans = salesPlanAdminStudentService.getUserSalesPlans(authentication);
        return ResponseEntity.ok(salesPlans);
    }
}