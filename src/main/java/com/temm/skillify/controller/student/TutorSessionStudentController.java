package com.temm.skillify.controller.student;

import com.temm.skillify.model.entity.TutorSession;
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
@PreAuthorize("hasRole('ESTUDANTE')")
public class TutorSessionStudentController {

    private final TutorSessionService sessionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<TutorSession>> getMyTutorSessions(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(sessionService.findByStudent(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorSession> getTutorSessionById(@PathVariable String id, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        
        return sessionService.findById(id)
                .filter(session -> session.getStudent().equals(student))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TutorSession> requestTutorSession(@RequestBody TutorSession session, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        session.setStudent(student);
        
        // Verify mentor is a mentor of one of the student's classrooms
        User mentor = session.getMentor();
        boolean isMentorOfStudentClassroom = sessionService.isMentorOfStudentClassroom(student, mentor);
        
        if (!isMentorOfStudentClassroom) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(sessionService.save(session));
    }
}
