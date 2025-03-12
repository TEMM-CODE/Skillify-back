package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.service.QuestionAdminService;
import com.temm.skillify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class QuestionAdminController {
    
    private final QuestionAdminService questionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable String id) {
        return questionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByMentor(@PathVariable String mentorId) {
        return ResponseEntity.ok(questionService.findByMentorId(mentorId));
    }
    
    @PostMapping
    public ResponseEntity<QuestionResponseDTO> createQuestion(
            @RequestBody QuestionCreateDTO questionCreateDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        QuestionResponseDTO savedQuestion = questionService.create(questionCreateDTO);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable String id,
            @RequestBody QuestionCreateDTO questionCreateDTO,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return questionService.update(id, questionCreateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        boolean deleted = questionService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}