package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.TutorSessionAdminService;
import com.temm.skillify.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tutor-sessions")
@RequiredArgsConstructor
public class TutorSessionAdminController {
    
    private final TutorSessionAdminService tutorSessionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<TutorSessionResponseDTO>> getAllSessions() {
        return ResponseEntity.ok(tutorSessionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TutorSessionResponseDTO> getSessionById(@PathVariable String id) {
        return tutorSessionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByMentor(@PathVariable String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor not found with ID: " + mentorId));
        
        return ResponseEntity.ok(tutorSessionService.findByMentor(mentor));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByStudent(@PathVariable String studentId) {
        User student = userService.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        
        return ResponseEntity.ok(tutorSessionService.findByStudent(student));
    }
    
    @GetMapping("/mentor/{mentorId}/date/{date}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByMentorAndDate(
            @PathVariable String mentorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor not found with ID: " + mentorId));
        
        return ResponseEntity.ok(tutorSessionService.findByMentorAndDate(mentor, date));
    }
    
    @GetMapping("/student/{studentId}/date/{date}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByStudentAndDate(
            @PathVariable String studentId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        User student = userService.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        
        return ResponseEntity.ok(tutorSessionService.findByStudentAndDate(student, date));
    }
    
    @PostMapping
    public ResponseEntity<TutorSessionResponseDTO> createSession(
            @RequestBody TutorSessionCreateDTO tutorSessionCreateDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        TutorSessionResponseDTO createdSession = tutorSessionService.create(tutorSessionCreateDTO);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TutorSessionResponseDTO> updateSession(
            @PathVariable String id,
            @RequestBody TutorSessionCreateDTO tutorSessionCreateDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        try {
            TutorSessionResponseDTO updatedSession = tutorSessionService.update(id, tutorSessionCreateDTO);
            return ResponseEntity.ok(updatedSession);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        try {
            tutorSessionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}