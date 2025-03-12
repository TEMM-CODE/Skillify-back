package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.EssayCorrectionCreateDTO;
import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.EssayCorrectionAdminService;
import com.temm.skillify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/essay-corrections")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EssayCorrectionAdminController {
    
    private final EssayCorrectionAdminService essayCorrectionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<EssayCorrectionResponseDTO>> getAllCorrections() {
        return ResponseEntity.ok(essayCorrectionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EssayCorrectionResponseDTO> getCorrectionById(@PathVariable String id) {
        return essayCorrectionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<EssayCorrectionResponseDTO>> getCorrectionsByMentor(@PathVariable String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        
        return ResponseEntity.ok(essayCorrectionService.findByMentor(mentor));
    }
    
    @PostMapping
    public ResponseEntity<EssayCorrectionResponseDTO> createCorrection(
            @RequestBody EssayCorrectionCreateDTO essayCorrectionDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        EssayCorrectionResponseDTO savedCorrection = essayCorrectionService.save(essayCorrectionDTO);
        return new ResponseEntity<>(savedCorrection, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EssayCorrectionResponseDTO> updateCorrection(
            @PathVariable String id,
            @RequestBody EssayCorrectionCreateDTO essayCorrectionDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return essayCorrectionService.update(id, essayCorrectionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        if (essayCorrectionService.findById(id).isPresent()) {
            essayCorrectionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}