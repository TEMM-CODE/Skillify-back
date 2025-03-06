package com.temm.skillify.controller.admin;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.service.EssayExecutionAdminService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/essay-executions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EssayExecutionAdminController {
    
    private final EssayExecutionAdminService essayExecutionAdminService;
    
    @GetMapping
    public ResponseEntity<List<EssayExecution>> getAllExecutions() {
        return ResponseEntity.ok(essayExecutionAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EssayExecution> getExecutionById(@PathVariable String id) {
        return essayExecutionAdminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EssayExecution>> getExecutionsByStudent(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(essayExecutionAdminService.findByStudent(studentId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<EssayExecution> createExecution(
            @RequestBody EssayExecution execution,
            @RequestParam String studentId,
            @RequestParam String essayId) {
        try {
            EssayExecution created = essayExecutionAdminService.create(execution, studentId, essayId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EssayExecution> updateExecution(
            @PathVariable String id,
            @RequestBody EssayExecution execution,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String essayId) {
        try {
            EssayExecution updated = essayExecutionAdminService.update(id, execution, studentId, essayId);
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