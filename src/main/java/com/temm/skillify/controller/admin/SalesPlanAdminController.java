package com.temm.skillify.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.SalesPlanAdminService;
import com.temm.skillify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sales-plans")
@RequiredArgsConstructor
public class SalesPlanAdminController {
    
    private final SalesPlanAdminService salesPlanService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<SalesPlan>> getAllPlans() {
        return ResponseEntity.ok(salesPlanService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalesPlan> getPlanById(@PathVariable String id) {
        return salesPlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SalesPlan>> getPlansByType(@PathVariable PlanType type) {
        return ResponseEntity.ok(salesPlanService.findByType(type));
    }
    
    @PostMapping
    public ResponseEntity<SalesPlan> createPlan(
            @RequestBody SalesPlan salesPlan,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        SalesPlan savedPlan = salesPlanService.save(salesPlan);
        return new ResponseEntity<>(savedPlan, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SalesPlan> updatePlan(
            @PathVariable String id,
            @RequestBody SalesPlan salesPlan,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return salesPlanService.findById(id)
                .map(existingPlan -> {
                    existingPlan.setName(salesPlan.getName());
                    existingPlan.setDescription(salesPlan.getDescription());
                    existingPlan.setPrice(salesPlan.getPrice());
                    existingPlan.setType(salesPlan.getType());
                    existingPlan.setResources(salesPlan.getResources());
                    
                    SalesPlan updatedPlan = salesPlanService.save(existingPlan);
                    return ResponseEntity.ok(updatedPlan);
                })
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