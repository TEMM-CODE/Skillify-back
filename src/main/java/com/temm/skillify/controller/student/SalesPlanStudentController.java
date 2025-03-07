package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.SalesPlanStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/sales-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class SalesPlanStudentController {

    private final SalesPlanStudentService salesPlanStudentService;

    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllSalesPlans() {
        return ResponseEntity.ok(salesPlanStudentService.getAllSalesPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getSalesPlanById(@PathVariable String id) {
        return ResponseEntity.ok(salesPlanStudentService.getSalesPlanById(id));
    }

    @GetMapping("/type/{planType}")
    public ResponseEntity<List<SalesPlanResponseDTO>> getSalesPlansByType(@PathVariable PlanType planType) {
        return ResponseEntity.ok(salesPlanStudentService.getSalesPlansByType(planType));
    }
}