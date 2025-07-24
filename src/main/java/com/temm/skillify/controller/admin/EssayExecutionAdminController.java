package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.service.EssayExecutionAdminService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/essay-executions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EssayExecutionAdminController {
    private final EssayExecutionAdminService essayExecutionAdminService;
    
    @GetMapping
    public ResponseEntity<List<EssayExecutionResponseDTO>> getAllExecutions(Authentication authentication) {
        return ResponseEntity.ok(essayExecutionAdminService.findAll(authentication));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EssayExecutionResponseDTO> getExecutionById(@PathVariable String id) {
        return essayExecutionAdminService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EssayExecutionResponseDTO>> getExecutionsByStudent(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(essayExecutionAdminService.findByStudent(studentId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<EssayExecutionResponseDTO> createExecution(@RequestBody EssayExecutionCreateDTO createDTO) {
        try {
            EssayExecutionResponseDTO created = essayExecutionAdminService.create(createDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EssayExecutionResponseDTO> updateExecution(
        @PathVariable String id,
        @RequestBody EssayExecutionCreateDTO updateDTO) {
        try {
            EssayExecutionResponseDTO updated = essayExecutionAdminService.update(id, updateDTO);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecution(@PathVariable String id) {
        try {
            essayExecutionAdminService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}