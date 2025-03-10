package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
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
    public ResponseEntity<List<TutorSessionResponseDTO>> getAllSessions(Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findAllByMentor(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorSessionResponseDTO> getSessionById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByIdAndMentor(id, authentication));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByMentorAndDate(date, authentication));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TutorSessionResponseDTO>> getSessionsByStudent(
            @PathVariable String studentId,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.findByMentorAndStudent(studentId, authentication));
    }

    @PostMapping
    public ResponseEntity<TutorSessionResponseDTO> createSession(@RequestBody TutorSessionCreateDTO dto, Authentication authentication) {
        return new ResponseEntity<>(tutorSessionService.create(dto, authentication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorSessionResponseDTO> updateSession(
            @PathVariable String id,
            @RequestBody TutorSessionCreateDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(tutorSessionService.update(id, dto, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id, Authentication authentication) {
        tutorSessionService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}