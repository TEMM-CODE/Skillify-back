package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.service.QuestionStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ESTUDANTE')")
public class QuestionStudentController {

    private final QuestionStudentService questionStudentService;

    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionStudentService.getAllAvailableQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable String id) {
        return ResponseEntity.ok(questionStudentService.getQuestionById(id));
    }

    @GetMapping("/practice/{practiceId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByPractice(@PathVariable String practiceId) {
        return ResponseEntity.ok(questionStudentService.getQuestionsByPractice(practiceId));
    }
}