package com.temm.skillify.controller.mentor;



import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/practices")
@RequiredArgsConstructor
public class PracticeMentorController {

    private final PracticeService practiceService;

    @GetMapping
    public ResponseEntity<List<Practice>> getAllPractices(Authentication authentication) {
        return ResponseEntity.ok(practiceService.findAllByMentor(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practice> getPracticeById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(practiceService.findByIdAndMentor(id, authentication));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Practice>> getPracticesByClassroom(@PathVariable String classroomId, Authentication authentication) {
        return ResponseEntity.ok(practiceService.findByClassroomAndMentor(classroomId, authentication));
    }

    @PostMapping
    public ResponseEntity<Practice> createPractice(@RequestBody Practice practice, Authentication authentication) {
        return new ResponseEntity<>(practiceService.create(practice, authentication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practice> updatePractice(@PathVariable String id, @RequestBody Practice practice, Authentication authentication) {
        return ResponseEntity.ok(practiceService.update(id, practice, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable String id, Authentication authentication) {
        practiceService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{practiceId}/questions/{questionId}")
    public ResponseEntity<Practice> addQuestionToPractice(
            @PathVariable String practiceId,
            @PathVariable String questionId,
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.addQuestion(practiceId, questionId, authentication));
    }

    @DeleteMapping("/{practiceId}/questions/{questionId}")
    public ResponseEntity<Practice> removeQuestionFromPractice(
            @PathVariable String practiceId,
            @PathVariable String questionId,
            Authentication authentication) {
        return ResponseEntity.ok(practiceService.removeQuestion(practiceId, questionId, authentication));
    }
}
