package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.service.PracticeExecutionStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/practice-executions")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
@RequiredArgsConstructor
public class PracticeExecutionStudentController {

    private final PracticeExecutionStudentService studentService;

    @GetMapping
    public ResponseEntity<List<PracticeExecutionResponseDTO>> getAllStudentPracticeExecutions() {
        return ResponseEntity.ok(studentService.getAllStudentPracticeExecutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticeExecutionResponseDTO> getPracticeExecutionById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getPracticeExecutionById(id));
    }

    @PostMapping
    public ResponseEntity<PracticeExecutionResponseDTO> createPracticeExecution(
            @RequestBody PracticeExecutionCreateDTO createDTO) {
        return ResponseEntity.ok(studentService.createPracticeExecution(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticeExecutionResponseDTO> updatePracticeExecution(
            @PathVariable String id,
            @RequestBody PracticeExecutionCreateDTO updateDTO) {
        return ResponseEntity.ok(studentService.updatePracticeExecution(id, updateDTO));
    }
}