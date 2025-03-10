package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.service.SalesPlanService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/sales-plans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class SalesPlanMentorController {
    @Autowired
    private SalesPlanService salesPlanService;


    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllPlans() {
        return ResponseEntity.ok(salesPlanService.findAllDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getPlanById(@PathVariable String id) {
        return ResponseEntity.ok(salesPlanService.findDTOById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SalesPlanResponseDTO>> getPlansByType(@PathVariable PlanType type) {
        return ResponseEntity.ok(salesPlanService.findDTOsByType(type));
    }
}