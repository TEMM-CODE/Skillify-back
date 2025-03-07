package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.service.CourseStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ESTUDANTE')")
public class CourseStudentController {

    private final CourseStudentService courseStudentService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseStudentService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseStudentService.getCourseById(id));
    }

    @GetMapping("/enrolled")
    public ResponseEntity<List<CourseResponseDTO>> getEnrolledCourses(Authentication authentication) {
        return ResponseEntity.ok(courseStudentService.getEnrolledCourses(authentication));
    }

    @GetMapping("/{courseId}/categories")
    public ResponseEntity<List<CourseLessonCategoryResponseDTO>> getCourseLessonCategories(@PathVariable String courseId) {
        return ResponseEntity.ok(courseStudentService.getCourseLessonCategories(courseId));
    }

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<CourseLessonResponseDTO>> getCourseLessons(@PathVariable String courseId) {
        return ResponseEntity.ok(courseStudentService.getCourseLessons(courseId));
    }

    @GetMapping("/categories/{categoryId}/lessons")
    public ResponseEntity<List<CourseLessonResponseDTO>> getLessonsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(courseStudentService.getLessonsByCategory(categoryId));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Void> enrollInCourse(@PathVariable String courseId, Authentication authentication) {
        courseStudentService.enrollInCourse(courseId, authentication);
        return ResponseEntity.ok().build();
    }
}