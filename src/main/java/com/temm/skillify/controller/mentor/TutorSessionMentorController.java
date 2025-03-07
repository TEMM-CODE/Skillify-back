package com.temm.skillify.controller.mentor;



import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.service.TutorSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/mentor/tutor-sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class TutorSessionMentorController {

    private final TutorSessionService tutorSessionService;

    @GetMapping
    public ResponseEntity<List<TutorSession>> getAllSessions(Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findAllByMentor(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorSession> getSessionById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByIdAndMentor(id, authentication));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TutorSession>> getSessionsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByMentorAndDate(date, authentication));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TutorSession>> getSessionsByStudent(
            @PathVariable String studentId,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByMentorAndStudent(studentId, authentication));
    }

    @PostMapping
    public ResponseEntity<TutorSession> createSession(@RequestBody TutorSession session, Authentication authentication) {
        return new ResponseEntity<>(tutorSessionService.create(session, authentication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorSession> updateSession(
            @PathVariable String id,
            @RequestBody TutorSession session,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.update(id, session, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id, Authentication authentication) {
        tutorSessionService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
