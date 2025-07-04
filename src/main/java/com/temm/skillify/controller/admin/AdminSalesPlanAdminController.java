package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.SalesPlanAdminCreateDTO;
import com.temm.skillify.model.dto.request.SalesPlanAdminMembershipEventCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminMembershipEventReturnDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.service.admin.AdminSalesPlanAdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pacotes")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminSalesPlanAdminController {

    private final AdminSalesPlanAdminService adminSalesPlanAdminService;

    // SalesPlanAdmin endpoints
    @GetMapping
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getAllSalesPlans() {
        return ResponseEntity.ok(adminSalesPlanAdminService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPlanAdminReturnDTO> getSalesPlanById(@PathVariable String id) {
        SalesPlanAdminReturnDTO salesPlan = adminSalesPlanAdminService.getById(id);
        return salesPlan != null ? ResponseEntity.ok(salesPlan) : ResponseEntity.notFound().build();
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<SalesPlanAdminReturnDTO>> getSalesPlansByCreator(@PathVariable String creatorId) {
        return ResponseEntity.ok(adminSalesPlanAdminService.getByCreator(creatorId));
    }

    @PostMapping
    public ResponseEntity<SalesPlanAdminReturnDTO> createSalesPlan(
            @RequestBody SalesPlanAdminCreateDTO createDTO,
            @RequestParam String creatorId) {
        return ResponseEntity.ok(adminSalesPlanAdminService.create(createDTO, creatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPlanAdminReturnDTO> updateSalesPlan(
            @PathVariable String id,
            @RequestBody SalesPlanAdminCreateDTO updateDTO) {
        return ResponseEntity.ok(adminSalesPlanAdminService.edit(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesPlan(@PathVariable String id) {
        adminSalesPlanAdminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // SalesPlanAdminMembershipEvent endpoints
    @GetMapping("/membership-events")
    public ResponseEntity<List<SalesPlanAdminMembershipEventReturnDTO>> getAllMembershipEvents() {
        return ResponseEntity.ok(adminSalesPlanAdminService.getAllMembershipEvents());
    }

    @GetMapping("/membership-events/{id}")
    public ResponseEntity<SalesPlanAdminMembershipEventReturnDTO> getMembershipEventById(@PathVariable String id) {
        SalesPlanAdminMembershipEventReturnDTO event = adminSalesPlanAdminService.getMembershipEventById(id);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @PostMapping("/membership-events")
    public ResponseEntity<SalesPlanAdminMembershipEventReturnDTO> createMembershipEvent(
            @RequestBody SalesPlanAdminMembershipEventCreateDTO createDTO) {
        return ResponseEntity.ok(adminSalesPlanAdminService.createMembershipEvent(createDTO));
    }

    @PutMapping("/membership-events/{id}")
    public ResponseEntity<SalesPlanAdminMembershipEventReturnDTO> updateMembershipEvent(
            @PathVariable String id,
            @RequestBody SalesPlanAdminMembershipEventCreateDTO updateDTO) {
        return ResponseEntity.ok(adminSalesPlanAdminService.updateMembershipEvent(id, updateDTO));
    }

    @DeleteMapping("/membership-events/{id}")
    public ResponseEntity<Void> deleteMembershipEvent(@PathVariable String id) {
        adminSalesPlanAdminService.deleteMembershipEvent(id);
        return ResponseEntity.noContent().build();
    }
}