package com.temm.skillify.controller.mentor;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.service.ClassroomAccessTokenService;
import com.temm.skillify.service.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/classroom-tokens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MENTOR')")
public class ClassroomAccessTokenMentorController {

    private final ClassroomAccessTokenService tokenService;
    private final ClassroomService classroomService;

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<ClassroomAccessToken>> getTokensByClassroom(@PathVariable String classroomId, Authentication authentication) {
        // Verify that the mentor owns this classroom
        return classroomService.findByIdAndMentorEmail(classroomId, authentication.getName())
                .map(classroom -> ResponseEntity.ok(tokenService.findByClassroomId(classroomId)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/classroom/{classroomId}")
    public ResponseEntity<ClassroomAccessToken> generateToken(@PathVariable String classroomId, Authentication authentication) {
        // Verify that the mentor owns this classroom
        return classroomService.findByIdAndMentorEmail(classroomId, authentication.getName())
                .map(classroom -> ResponseEntity.ok(tokenService.generateToken(classroomId)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable String id, Authentication authentication) {
        return tokenService.findById(id)
                .flatMap(token -> classroomService.findByIdAndMentorEmail(token.getClassroom().getId(), authentication.getName())
                        .map(classroom -> {
                            tokenService.deleteById(id);
                            return ResponseEntity.ok().<Void>build();
                        }))
                .orElse(ResponseEntity.notFound().build());
    }
}