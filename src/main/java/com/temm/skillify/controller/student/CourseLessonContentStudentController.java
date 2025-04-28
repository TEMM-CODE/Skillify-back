package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.service.CourseLessonContentStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/course-lesson-content")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class CourseLessonContentStudentController {

    private final CourseLessonContentStudentService contentStudentService;

    @GetMapping
    public ResponseEntity<List<CourseLessonContentResponseDTO>> getAllContent(Authentication authentication) {
        return ResponseEntity.ok(contentStudentService.getAllCourseLessonContentsForStudent(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonContentResponseDTO> getContentById(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(contentStudentService.getCourseLessonContentById(id, authentication));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<CourseLessonContentResponseDTO>> getContentByLessonId(
            @PathVariable String lessonId,
            Authentication authentication) {
        return ResponseEntity.ok(contentStudentService.getCourseLessonContentsByLessonId(lessonId, authentication));
    }
}