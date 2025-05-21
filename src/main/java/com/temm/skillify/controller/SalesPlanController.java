package com.temm.skillify.controller;

import com.temm.skillify.model.dto.response.SalesPlanMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.service.unauthenticated.SalesPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-plans")
@RequiredArgsConstructor
public class SalesPlanController {

    private final SalesPlanService salesPlanService;

    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllSalesPlans() {
        return ResponseEntity.ok(salesPlanService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getSalesPlanById(@PathVariable String id) {
        SalesPlanResponseDTO salesPlan = salesPlanService.getById(id);
        return salesPlan != null ? ResponseEntity.ok(salesPlan) : ResponseEntity.notFound().build();
    }

    @GetMapping("/membership-events")
    public ResponseEntity<List<SalesPlanMembershipEventReturnDTO>> getAllMembershipEvents() {
        return ResponseEntity.ok(salesPlanService.getAllMembershipEvents());
    }

    @GetMapping("/membership-events/{id}")
    public ResponseEntity<SalesPlanMembershipEventReturnDTO> getMembershipEventById(@PathVariable String id) {
        SalesPlanMembershipEventReturnDTO event = salesPlanService.getMembershipEventById(id);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }
}