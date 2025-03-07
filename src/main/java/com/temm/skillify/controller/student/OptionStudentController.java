package com.temm.skillify.controller.student;



import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.service.OptionStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/options")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class OptionStudentController {
    
    private final OptionStudentService optionService;
    
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionResponseDTO>> getOptionsByQuestion(
            @PathVariable String questionId,
            Authentication authentication) {
        return ResponseEntity.ok(optionService.getOptionsByQuestion(questionId, authentication));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OptionResponseDTO> getOptionById(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(optionService.getOptionById(id, authentication));
    }
}