package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.service.EssayCorrectionStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/essay-corrections")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class EssayCorrectionStudentController {
    
    private final EssayCorrectionStudentService essayCorrectionService;
    
    @GetMapping
    public ResponseEntity<List<EssayCorrectionResponseDTO>> getAllEssayCorrections(Authentication authentication) {
        return ResponseEntity.ok(essayCorrectionService.getAllEssayCorrectionsForStudent(authentication));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EssayCorrectionResponseDTO> getEssayCorrectionById(
            @PathVariable String id, 
            Authentication authentication) {
        return ResponseEntity.ok(essayCorrectionService.getEssayCorrectionById(id, authentication));
    }
    
    @GetMapping("/essay-execution/{executionId}")
    public ResponseEntity<EssayCorrectionResponseDTO> getEssayCorrectionByExecution(
            @PathVariable String executionId,
            Authentication authentication) {
        return ResponseEntity.ok(essayCorrectionService.getEssayCorrectionByExecutionId(executionId, authentication));
    }
}