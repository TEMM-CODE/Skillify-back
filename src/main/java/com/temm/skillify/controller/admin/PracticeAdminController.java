package com.temm.skillify.controller.admin;


import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.service.PracticeAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/practices")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class PracticeAdminController {
    
    private final PracticeAdminService practiceAdminService;
    
    @GetMapping
    public ResponseEntity<List<Practice>> getAllPractices() {
        return ResponseEntity.ok(practiceAdminService.findAllPractices());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Practice> getPracticeById(@PathVariable String id) {
        return practiceAdminService.findPracticeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Practice> createPractice(@RequestBody Practice practice) {
        return ResponseEntity.ok(practiceAdminService.createPractice(practice));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Practice> updatePractice(@PathVariable String id, @RequestBody Practice practice) {
        return ResponseEntity.ok(practiceAdminService.updatePractice(id, practice));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable String id) {
        practiceAdminService.deletePractice(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(practiceAdminService.findAllQuestions());
    }
    
    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
        return practiceAdminService.findQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(practiceAdminService.createQuestion(question));
    }
    
    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable String id, @RequestBody Question question) {
        return ResponseEntity.ok(practiceAdminService.updateQuestion(id, question));
    }
    
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        practiceAdminService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<Option> addOptionToQuestion(
            @PathVariable String questionId,
            @RequestBody Option option) {
        return ResponseEntity.ok(practiceAdminService.addOptionToQuestion(questionId, option));
    }
    
    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable String optionId) {
        practiceAdminService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{practiceId}/questions")
    public ResponseEntity<Practice> updatePracticeQuestions(
            @PathVariable String practiceId,
            @RequestBody Set<Question> questions) {
        return ResponseEntity.ok(practiceAdminService.updatePracticeQuestions(practiceId, questions));
    }
}