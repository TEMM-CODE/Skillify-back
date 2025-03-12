package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.TutorSessionService;
import com.temm.skillify.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/tutor-sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class TutorSessionStudentController {
    private final TutorSessionService sessionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<TutorSessionResponseDTO>> getMyTutorSessions(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        return ResponseEntity.ok(sessionService.findByStudent(student));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TutorSessionResponseDTO> getTutorSessionById(@PathVariable String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        return sessionService.findById(id)
                .filter(session -> session.getStudent().getId().equals(student.getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TutorSessionResponseDTO> requestTutorSession(@RequestBody TutorSessionCreateDTO sessionDTO, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // Make sure the student ID is set in the DTO
        sessionDTO.setStudentId(student.getId());
        
        // Get the mentor from the DTO
        User mentor = null;
        if (sessionDTO.getMentorId() != null && !sessionDTO.getMentorId().isEmpty()) {
            // We'll need to get the mentor using UserService, but we're just validating here
            // Note: This assumes UserService has a findById method, which isn't shown in the pasted code
            mentor = userService.findById(sessionDTO.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Mentor not found"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        // Verify mentor is a mentor of one of the student's classrooms
        boolean isMentorOfStudentClassroom = sessionService.isMentorOfStudentClassroom(student, mentor);
        if (!isMentorOfStudentClassroom) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create the session using the service
        TutorSessionResponseDTO createdSession = sessionService.create(sessionDTO, authentication);
        return ResponseEntity.ok(createdSession);
    }
}