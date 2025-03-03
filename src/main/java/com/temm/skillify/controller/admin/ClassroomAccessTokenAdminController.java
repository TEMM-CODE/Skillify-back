package com.temm.skillify.controller.admin;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.service.ClassroomAccessTokenService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/classroom-tokens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ClassroomAccessTokenAdminController {

    private final ClassroomAccessTokenService tokenService;

    @GetMapping
    public ResponseEntity<List<ClassroomAccessToken>> getAllTokens() {
        return ResponseEntity.ok(tokenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomAccessToken> getTokenById(@PathVariable String id) {
        return tokenService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<ClassroomAccessToken>> getTokensByClassroom(@PathVariable String classroomId) {
        return ResponseEntity.ok(tokenService.findByClassroomId(classroomId));
    }

    @PostMapping("/classroom/{classroomId}")
    public ResponseEntity<ClassroomAccessToken> generateToken(@PathVariable String classroomId) {
        return ResponseEntity.ok(tokenService.generateToken(classroomId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable String id) {
        tokenService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/revoke/{token}")
    public ResponseEntity<Void> revokeToken(@PathVariable String token) {
        tokenService.revokeToken(token);
        return ResponseEntity.ok().build();
    }
}