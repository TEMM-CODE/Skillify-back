package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.TutorSessionAdminService;
import com.temm.skillify.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tutor-sessions")
@RequiredArgsConstructor
public class TutorSessionAdminController {
    
    private final TutorSessionAdminService tutorSessionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<TutorSession>> getAllSessions() {
        return ResponseEntity.ok(tutorSessionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TutorSession> getSessionById(@PathVariable String id) {
        return tutorSessionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<TutorSession>> getSessionsByMentor(@PathVariable String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        
        return ResponseEntity.ok(tutorSessionService.findByMentor(mentor));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TutorSession>> getSessionsByStudent(@PathVariable String studentId) {
        User student = userService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return ResponseEntity.ok(tutorSessionService.findByStudent(student));
    }
    
    @GetMapping("/mentor/{mentorId}/date/{date}")
    public ResponseEntity<List<TutorSession>> getSessionsByMentorAndDate(
            @PathVariable String mentorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        
        return ResponseEntity.ok(tutorSessionService.findByMentorAndDate(mentor, date));
    }
    
    @GetMapping("/student/{studentId}/date/{date}")
    public ResponseEntity<List<TutorSession>> getSessionsByStudentAndDate(
            @PathVariable String studentId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        User student = userService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return ResponseEntity.ok(tutorSessionService.findByStudentAndDate(student, date));
    }
    
    @PostMapping
    public ResponseEntity<TutorSession> createSession(
            @RequestBody TutorSession tutorSession,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        TutorSession savedSession = tutorSessionService.save(tutorSession);
        return new ResponseEntity<>(savedSession, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TutorSession> updateSession(
            @PathVariable String id,
            @RequestBody TutorSession tutorSession,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return tutorSessionService.findById(id)
                .map(existingSession -> {
                    existingSession.setMentor(tutorSession.getMentor());
                    existingSession.setStudent(tutorSession.getStudent());
                    existingSession.setTitle(tutorSession.getTitle());
                    existingSession.setDate(tutorSession.getDate());
                    existingSession.setDateHour(tutorSession.getDateHour());
                    existingSession.setType(tutorSession.getType());
                    existingSession.setLink(tutorSession.getLink());
                    
                    TutorSession updatedSession = tutorSessionService.save(existingSession);
                    return ResponseEntity.ok(updatedSession);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        if (tutorSessionService.findById(id).isPresent()) {
            tutorSessionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}