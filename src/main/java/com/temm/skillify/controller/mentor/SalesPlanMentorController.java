package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.model.enums.SalesPlanMembershipType;
import com.temm.skillify.service.mentor.SalesPlanMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/sales-plans")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class SalesPlanMentorController {

    @Autowired
    private SalesPlanMentorService salesPlanMentorService;

    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllSalesPlans(Authentication authentication) {
        return ResponseEntity.ok(salesPlanMentorService.getAllSalesPlans(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getSalesPlanById(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(salesPlanMentorService.getSalesPlanById(authentication, id));
    }

    @GetMapping("/type/{planType}")
    public ResponseEntity<List<SalesPlanResponseDTO>> getSalesPlansByType(Authentication authentication, @PathVariable PlanType planType) {
        return ResponseEntity.ok(salesPlanMentorService.getSalesPlansByType(authentication, planType));
    }

    @PostMapping
    public ResponseEntity<SalesPlanResponseDTO> createSalesPlan(Authentication authentication, @RequestBody SalesPlanCreateDTO createDTO) {
        return new ResponseEntity<>(salesPlanMentorService.createSalesPlan(authentication, createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> updateSalesPlan(Authentication authentication, @PathVariable String id, @RequestBody SalesPlanCreateDTO updateDTO) {
        return ResponseEntity.ok(salesPlanMentorService.updateSalesPlan(authentication, id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesPlan(Authentication authentication, @PathVariable String id) {
        salesPlanMentorService.deleteSalesPlan(authentication, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/membership-events/{eventId}")
    public ResponseEntity<SalesPlanMembershipEvent> getSalesPlanMembershipEvent(Authentication authentication, @PathVariable String eventId) {
        return ResponseEntity.ok(salesPlanMentorService.getSalesPlanMembershipEvent(authentication, eventId));
    }

    @PutMapping("/membership-events/{eventId}")
    public ResponseEntity<SalesPlanMembershipEvent> updateSalesPlanMembershipEvent(Authentication authentication, @PathVariable String eventId, @RequestBody SalesPlanMembershipType newStatus) {
        return ResponseEntity.ok(salesPlanMentorService.updateSalesPlanMembershipEvent(authentication, eventId, newStatus));
    }

    @DeleteMapping("/membership-events/{eventId}")
    public ResponseEntity<Void> deleteSalesPlanMembershipEvent(Authentication authentication, @PathVariable String eventId) {
        salesPlanMentorService.deleteSalesPlanMembershipEvent(authentication, eventId);
        return ResponseEntity.noContent().build();
    }
}