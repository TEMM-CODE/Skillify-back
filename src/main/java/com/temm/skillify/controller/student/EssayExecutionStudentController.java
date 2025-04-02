package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.service.EssayExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/essay-executions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class EssayExecutionStudentController {
    private final EssayExecutionService essayExecutionService;

    @GetMapping
    public ResponseEntity<List<EssayExecutionResponseDTO>> getAllMyEssayExecutions(Authentication authentication) {
        return ResponseEntity.ok(essayExecutionService.findAllDTOsByStudentEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayExecutionResponseDTO> getEssayExecutionById(@PathVariable String id, Authentication authentication) {
        return essayExecutionService.findDTOByIdAndStudentEmail(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EssayExecutionResponseDTO> createEssayExecution(@RequestBody EssayExecutionCreateDTO createDTO, Authentication authentication) {
        return ResponseEntity.ok(essayExecutionService.saveForStudent(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EssayExecutionResponseDTO> updateEssayExecution(@PathVariable String id, @RequestBody EssayExecutionCreateDTO updateDTO, Authentication authentication) {
        return essayExecutionService.findDTOByIdAndStudentEmail(id, authentication.getName())
                .map(existingDTO -> ResponseEntity.ok(essayExecutionService.update(id, updateDTO)))
                .orElse(ResponseEntity.notFound().build());
    }
}