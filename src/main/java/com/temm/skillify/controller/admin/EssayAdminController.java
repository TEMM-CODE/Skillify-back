package com.temm.skillify.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.service.EssayAdminService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/essays")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EssayAdminController {
    
    private final EssayAdminService essayAdminService;
    
    @GetMapping
    public ResponseEntity<List<Essay>> getAllEssays() {
        return ResponseEntity.ok(essayAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Essay> getEssayById(@PathVariable String id) {
        return essayAdminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Essay>> getEssaysByClassroom(@PathVariable String classroomId) {
        try {
            return ResponseEntity.ok(essayAdminService.findByClassroom(classroomId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Essay> createEssay(
            @RequestBody Essay essay,
            @RequestParam String classroomId) {
        try {
            Essay created = essayAdminService.create(essay, classroomId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Essay> updateEssay(
            @PathVariable String id,
            @RequestBody Essay essay,
            @RequestParam(required = false) String classroomId) {
        try {
            Essay updated = essayAdminService.update(id, essay, classroomId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEssay(@PathVariable String id) {
        try {
            essayAdminService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}