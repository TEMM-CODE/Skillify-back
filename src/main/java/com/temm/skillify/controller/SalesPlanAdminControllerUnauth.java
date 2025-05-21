package com.temm.skillify.controller;

import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.unauthenticated.SalesPlanAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacotes-admin")
@RequiredArgsConstructor
public class SalesPlanAdminControllerUnauth {

    private final SalesPlanAdminService salesPlanAdminService;

    @GetMapping
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getAllSalesPlans() {
        List<SalesPlanAdminReturnDTO> salesPlans = salesPlanAdminService.getAll();
        return ResponseEntity.ok(salesPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanAdminReturnDTO> getSalesPlanById(@PathVariable String id) {
        SalesPlanAdminReturnDTO salesPlan = salesPlanAdminService.getById(id);
        if (salesPlan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(salesPlan);
    }

    @GetMapping("/usuario/{creatorId}")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getSalesPlansByCreator(@PathVariable String creatorId) {
        List<SalesPlanAdminReturnDTO> salesPlans = salesPlanAdminService.getByCreator(creatorId);
        return ResponseEntity.ok(salesPlans);
    }
}