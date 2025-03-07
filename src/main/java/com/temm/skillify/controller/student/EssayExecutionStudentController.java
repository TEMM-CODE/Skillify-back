package com.temm.skillify.controller.student;


import com.temm.skillify.model.entity.EssayExecution;
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
    public ResponseEntity<List<EssayExecution>> getAllMyEssayExecutions(Authentication authentication) {
        return ResponseEntity.ok(essayExecutionService.findAllByStudentEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayExecution> getEssayExecutionById(@PathVariable String id, Authentication authentication) {
        return essayExecutionService.findByIdAndStudentEmail(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EssayExecution> createEssayExecution(@RequestBody EssayExecution essayExecution, Authentication authentication) {
        return ResponseEntity.ok(essayExecutionService.saveForStudent(essayExecution, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EssayExecution> updateEssayExecution(@PathVariable String id, @RequestBody EssayExecution essayExecution, Authentication authentication) {
        return essayExecutionService.findByIdAndStudentEmail(id, authentication.getName())
                .map(existingEssayExecution -> {
                    essayExecution.setId(id);
                    return ResponseEntity.ok(essayExecutionService.save(essayExecution));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
