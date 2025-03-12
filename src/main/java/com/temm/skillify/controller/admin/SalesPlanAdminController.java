package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.SalesPlanAdminService;
import com.temm.skillify.service.UserService;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sales-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SalesPlanAdminController {
    private final SalesPlanAdminService salesPlanService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllPlans() {
        return ResponseEntity.ok(salesPlanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getPlanById(@PathVariable String id) {
        return salesPlanService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SalesPlanResponseDTO>> getPlansByType(@PathVariable PlanType type) {
        return ResponseEntity.ok(salesPlanService.findByType(type));
    }

    @PostMapping
    public ResponseEntity<SalesPlanResponseDTO> createPlan(
            @RequestBody SalesPlanCreateDTO salesPlanDTO,
            Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        SalesPlanResponseDTO savedPlan = salesPlanService.save(salesPlanDTO);
        return new ResponseEntity<>(savedPlan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> updatePlan(
            @PathVariable String id,
            @RequestBody SalesPlanCreateDTO salesPlanDTO,
            Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        return salesPlanService.update(id, salesPlanDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        if (salesPlanService.findById(id).isPresent()) {
            salesPlanService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}