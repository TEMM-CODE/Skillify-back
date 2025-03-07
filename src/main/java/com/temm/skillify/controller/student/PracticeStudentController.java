package com.temm.skillify.controller.student;



import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.service.PracticeStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/practices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class PracticeStudentController {
    
    private final PracticeStudentService practiceService;
    
    @GetMapping
    public ResponseEntity<List<PracticeResponseDTO>> getAllPractices(Authentication authentication) {
        return ResponseEntity.ok(practiceService.getAllPracticesForStudent(authentication));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PracticeResponseDTO> getPracticeById(
            @PathVariable String id, 
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.getPracticeById(id, authentication));
    }
    
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<PracticeResponseDTO>> getPracticesByClassroom(
            @PathVariable String classroomId,
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.getPracticesByClassroom(classroomId, authentication));
    }
}