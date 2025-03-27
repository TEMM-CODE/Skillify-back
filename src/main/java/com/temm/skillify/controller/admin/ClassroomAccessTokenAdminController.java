package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.ClassroomAccessTokenCreateDTO;
import com.temm.skillify.model.dto.response.ClassroomAccessTokenResponseDTO;
import com.temm.skillify.service.ClassroomAccessTokenService;
import com.temm.skillify.service.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/classroom-tokens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClassroomAccessTokenAdminController {

    private final ClassroomAccessTokenService tokenService;
    private final ClassroomService classroomService;

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<ClassroomAccessTokenResponseDTO>> getTokensByClassroom(@PathVariable String classroomId, Authentication authentication) {
        return ResponseEntity.ok(tokenService.findByClassroomId(classroomId));
    }

    @PostMapping("/classroom/{classroomId}")
    public ResponseEntity<ClassroomAccessTokenResponseDTO> generateToken(@PathVariable String classroomId, Authentication authentication) {
        return ResponseEntity.ok(tokenService.generateToken(classroomId));
    }
    
    @PostMapping
    public ResponseEntity<ClassroomAccessTokenResponseDTO> createToken(@RequestBody ClassroomAccessTokenCreateDTO createDTO, Authentication authentication) {
        return ResponseEntity.ok(tokenService.save(createDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable String id, Authentication authentication) {
        return tokenService.findById(id)
                .map(token -> {
                    tokenService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}