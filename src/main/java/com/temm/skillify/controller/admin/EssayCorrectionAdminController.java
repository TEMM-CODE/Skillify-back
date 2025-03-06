package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.EssayCorrectionAdminService;
import com.temm.skillify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/essay-corrections")
@RequiredArgsConstructor
public class EssayCorrectionAdminController {
    
    private final EssayCorrectionAdminService essayCorrectionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<EssayCorrection>> getAllCorrections() {
        return ResponseEntity.ok(essayCorrectionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EssayCorrection> getCorrectionById(@PathVariable String id) {
        return essayCorrectionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<EssayCorrection>> getCorrectionsByMentor(@PathVariable String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        
        return ResponseEntity.ok(essayCorrectionService.findByMentor(mentor));
    }
    
    @PostMapping
    public ResponseEntity<EssayCorrection> createCorrection(
            @RequestBody EssayCorrection essayCorrection,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        EssayCorrection savedCorrection = essayCorrectionService.save(essayCorrection);
        return new ResponseEntity<>(savedCorrection, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EssayCorrection> updateCorrection(
            @PathVariable String id,
            @RequestBody EssayCorrection essayCorrection,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return essayCorrectionService.findById(id)
                .map(existingCorrection -> {
                    // Update all fields
                    existingCorrection.setEssay(essayCorrection.getEssay());
                    existingCorrection.setMentor(essayCorrection.getMentor());
                    existingCorrection.setEssayExecution(essayCorrection.getEssayExecution());
                    existingCorrection.setEstruturaCoesaoComentario(essayCorrection.getEstruturaCoesaoComentario());
                    existingCorrection.setArgumentacaoComentario(essayCorrection.getArgumentacaoComentario());
                    existingCorrection.setConquistas(essayCorrection.getConquistas());
                    existingCorrection.setCompetencia1Score(essayCorrection.getCompetencia1Score());
                    existingCorrection.setCompetencia2Score(essayCorrection.getCompetencia2Score());
                    existingCorrection.setCompetencia3Score(essayCorrection.getCompetencia3Score());
                    existingCorrection.setCompetencia4Score(essayCorrection.getCompetencia4Score());
                    existingCorrection.setCompetencia5Score(essayCorrection.getCompetencia5Score());
                    
                    EssayCorrection updatedCorrection = essayCorrectionService.save(existingCorrection);
                    return ResponseEntity.ok(updatedCorrection);
                })
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