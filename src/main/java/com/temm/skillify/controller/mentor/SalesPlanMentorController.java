package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.SalesPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/sales-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class SalesPlanMentorController {

    private final SalesPlanService salesPlanService;

    @GetMapping
    public ResponseEntity<List<SalesPlan>> getAllPlans() {
        return ResponseEntity.ok(salesPlanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlan> getPlanById(@PathVariable String id) {
        return ResponseEntity.ok(salesPlanService.findById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SalesPlan>> getPlansByType(@PathVariable PlanType type) {
        return ResponseEntity.ok(salesPlanService.findByType(type));
    }
}