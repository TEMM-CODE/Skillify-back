package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.SalesPlanCreateDTO;
import com.temm.skillify.model.dto.request.SalesPlanMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanResponseDTO;
import com.temm.skillify.service.admin.AdminSalesPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sales-plans")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminSalesPlanController {

    private final AdminSalesPlanService adminSalesPlanService;

    // SalesPlan endpoints
    @GetMapping
    public ResponseEntity<List<SalesPlanResponseDTO>> getAllSalesPlans() {
        return ResponseEntity.ok(adminSalesPlanService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> getSalesPlanById(@PathVariable String id) {
        SalesPlanResponseDTO salesPlan = adminSalesPlanService.getById(id);
        return salesPlan != null ? ResponseEntity.ok(salesPlan) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SalesPlanResponseDTO> createSalesPlan(@RequestBody SalesPlanCreateDTO createDTO) {
        return ResponseEntity.ok(adminSalesPlanService.create(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPlanResponseDTO> updateSalesPlan(
            @PathVariable String id,
            @RequestBody SalesPlanCreateDTO updateDTO) {
        return ResponseEntity.ok(adminSalesPlanService.edit(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesPlan(@PathVariable String id) {
        adminSalesPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // SalesPlanMembershipEvent endpoints
    @GetMapping("/membership-events")
    public ResponseEntity<List<SalesPlanMembershipEventReturnDTO>> getAllMembershipEvents() {
        return ResponseEntity.ok(adminSalesPlanService.getAllMembershipEvents());
    }

    @GetMapping("/membership-events/{id}")
    public ResponseEntity<SalesPlanMembershipEventReturnDTO> getMembershipEventById(@PathVariable String id) {
        SalesPlanMembershipEventReturnDTO event = adminSalesPlanService.getMembershipEventById(id);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @PostMapping("/membership-events")
    public ResponseEntity<SalesPlanMembershipEventReturnDTO> createMembershipEvent(
            @RequestBody SalesPlanMembershipEventCreateDTO createDTO) {
        return ResponseEntity.ok(adminSalesPlanService.createMembershipEvent(createDTO));
    }

    @PutMapping("/membership-events/{id}")
    public ResponseEntity<SalesPlanMembershipEventReturnDTO> updateMembershipEvent(
            @PathVariable String id,
            @RequestBody SalesPlanMembershipEventCreateDTO updateDTO) {
        return ResponseEntity.ok(adminSalesPlanService.updateMembershipEvent(id, updateDTO));
    }

    @DeleteMapping("/membership-events/{id}")
    public ResponseEntity<Void> deleteMembershipEvent(@PathVariable String id) {
        adminSalesPlanService.deleteMembershipEvent(id);
        return ResponseEntity.noContent().build();
    }
}