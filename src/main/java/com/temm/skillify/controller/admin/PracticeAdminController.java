package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.PracticeCreateDTO;
import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/practices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PracticeAdminController {
    private final PracticeService practiceService;

    @GetMapping
    public ResponseEntity<List<PracticeResponseDTO>> getAllPractices(Authentication authentication) {
        return ResponseEntity.ok(practiceService.findAllByMentor(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticeResponseDTO> getPracticeById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(practiceService.findByIdAndMentor(id, authentication));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<PracticeResponseDTO>> getPracticesByClassroom(@PathVariable String classroomId, Authentication authentication) {
        return ResponseEntity.ok(practiceService.findByClassroomAndMentor(classroomId, authentication));
    }

    @PostMapping
    public ResponseEntity<PracticeResponseDTO> createPractice(@RequestBody PracticeCreateDTO practiceDTO, Authentication authentication) {
        return new ResponseEntity<>(practiceService.create(practiceDTO, authentication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticeResponseDTO> updatePractice(@PathVariable String id, @RequestBody PracticeCreateDTO practiceDTO, Authentication authentication) {
        return ResponseEntity.ok(practiceService.update(id, practiceDTO, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable String id, Authentication authentication) {
        practiceService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{practiceId}/questions/{questionId}")
    public ResponseEntity<PracticeResponseDTO> addQuestionToPractice(
            @PathVariable String practiceId,
            @PathVariable String questionId,
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.addQuestion(practiceId, questionId, authentication));
    }

    @DeleteMapping("/{practiceId}/questions/{questionId}")
    public ResponseEntity<PracticeResponseDTO> removeQuestionFromPractice(
            @PathVariable String practiceId,
            @PathVariable String questionId,
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.removeQuestion(practiceId, questionId, authentication));
    }
}