package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/mentor/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class QuestionMentorController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions(Authentication authentication) {
        return ResponseEntity.ok(questionService.findAllByMentor(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(questionService.findByIdAndMentor(id, authentication));
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question, Authentication authentication) {
        return new ResponseEntity<>(questionService.create(question, authentication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable String id, @RequestBody Question question, Authentication authentication) {
        return ResponseEntity.ok(questionService.update(id, question, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id, Authentication authentication) {
        questionService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<Question> addOptions(@PathVariable String id, @RequestBody Set<Option> options, Authentication authentication) {
        return ResponseEntity.ok(questionService.addOptions(id, options, authentication));
    }

    @PutMapping("/{questionId}/options/{optionId}")
    public ResponseEntity<Question> updateOption(
            @PathVariable String questionId,
            @PathVariable String optionId,
            @RequestBody Option option,
            Authentication authentication) {
        return ResponseEntity.ok(questionService.updateOption(questionId, optionId, option, authentication));
    }

    @DeleteMapping("/{questionId}/options/{optionId}")
    public ResponseEntity<Question> deleteOption(
            @PathVariable String questionId,
            @PathVariable String optionId,
            Authentication authentication) {
        return ResponseEntity.ok(questionService.deleteOption(questionId, optionId, authentication));
    }
}
