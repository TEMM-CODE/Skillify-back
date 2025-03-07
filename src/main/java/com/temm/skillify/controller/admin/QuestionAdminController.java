package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.OptionAdminService;
import com.temm.skillify.service.QuestionAdminService;
import com.temm.skillify.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class QuestionAdminController {
    
    private final QuestionAdminService questionService;
    private final OptionAdminService optionService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
        return questionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<Question>> getQuestionsByMentor(@PathVariable String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        
        return ResponseEntity.ok(questionService.findByMentor(mentor));
    }
    
    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestBody Question question,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        // Save the question first without options
        Set<Option> options = question.getOptions();
        question.setOptions(new HashSet<>());
        Question savedQuestion = questionService.save(question);
        
        // Save options with reference to the saved question
        if (options != null && !options.isEmpty()) {
            for (Option option : options) {
                option.setQuestion(savedQuestion);
                optionService.save(option);
            }
            
            // Refresh the question to include the saved options
            savedQuestion = questionService.findById(savedQuestion.getId()).orElse(savedQuestion);
        }
        
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable String id,
            @RequestBody Question question,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return questionService.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setTitle(question.getTitle());
                    existingQuestion.setMentor(question.getMentor());
                    
                    // Update options
                    Set<Option> currentOptions = existingQuestion.getOptions();
                    Set<Option> newOptions = question.getOptions();
                    
                    // Remove options not in the new set
                    if (currentOptions != null) {
                        currentOptions.removeIf(option -> 
                            newOptions == null || 
                            newOptions.stream().noneMatch(o -> 
                                o.getId() != null && o.getId().equals(option.getId())
                            )
                        );
                    }
                    
                    // Save the question with updated fields and remaining options
                    Question updatedQuestion = questionService.save(existingQuestion);
                    
                    // Add or update options
                    if (newOptions != null) {
                        for (Option option : newOptions) {
                            option.setQuestion(updatedQuestion);
                            optionService.save(option);
                        }
                        
                        // Refresh the question to include the updated options
                        updatedQuestion = questionService.findById(updatedQuestion.getId()).orElse(updatedQuestion);
                    }
                    
                    return ResponseEntity.ok(updatedQuestion);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        if (questionService.findById(id).isPresent()) {
            questionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}