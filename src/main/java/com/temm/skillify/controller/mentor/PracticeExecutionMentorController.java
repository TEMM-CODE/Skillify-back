package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.service.PracticeExecutionMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/practice-executions")
@PreAuthorize("hasRole('ROLE_MENTOR')")
@RequiredArgsConstructor
public class PracticeExecutionMentorController {

    private final PracticeExecutionMentorService mentorService;

    @GetMapping
    public ResponseEntity<List<PracticeExecutionResponseDTO>> getAllMentorPracticeExecutions() {
        return ResponseEntity.ok(mentorService.getAllMentorPracticeExecutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticeExecutionResponseDTO> getPracticeExecutionById(@PathVariable String id) {
        return ResponseEntity.ok(mentorService.getPracticeExecutionById(id));
    }

    @PostMapping
    public ResponseEntity<PracticeExecutionResponseDTO> createPracticeExecution(
            @RequestBody PracticeExecutionCreateDTO createDTO) {
        return ResponseEntity.ok(mentorService.createPracticeExecution(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticeExecutionResponseDTO> updatePracticeExecution(
            @PathVariable String id,
            @RequestBody PracticeExecutionCreateDTO updateDTO) {
        return ResponseEntity.ok(mentorService.updatePracticeExecution(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePracticeExecution(@PathVariable String id) {
        mentorService.deletePracticeExecution(id);
        return ResponseEntity.noContent().build();
    }
}