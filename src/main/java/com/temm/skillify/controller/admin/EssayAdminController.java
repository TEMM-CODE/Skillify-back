package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.request.EssayCreateDTO;
import com.temm.skillify.model.dto.response.EssayResponseDTO;
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
    public ResponseEntity<List<EssayResponseDTO>> getAllEssays() {
        return ResponseEntity.ok(essayAdminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayResponseDTO> getEssayById(@PathVariable String id) {
        return essayAdminService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<EssayResponseDTO>> getEssaysByClassroom(@PathVariable String classroomId) {
        try {
            return ResponseEntity.ok(essayAdminService.findByClassroom(classroomId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EssayResponseDTO> createEssay(@RequestBody EssayCreateDTO essayCreateDTO) {
        try {
            EssayResponseDTO created = essayAdminService.create(essayCreateDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EssayResponseDTO> updateEssay(
            @PathVariable String id,
            @RequestBody EssayCreateDTO essayCreateDTO) {
        try {
            EssayResponseDTO updated = essayAdminService.update(id, essayCreateDTO);
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