package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.service.CourseLessonCategoryStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/course-categories")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class CourseLessonCategoryStudentController {

    @Autowired
    private CourseLessonCategoryStudentService categoryService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonCategoryResponseDTO>> getCategoriesByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(categoryService.getCategoriesByCourse(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonCategoryResponseDTO> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}