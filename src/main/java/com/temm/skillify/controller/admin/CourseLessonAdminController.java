package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.service.CourseLessonAdminService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/course-lessons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CourseLessonAdminController {
    
    private final CourseLessonAdminService courseLessonAdminService;
    
    @GetMapping
    public ResponseEntity<List<CourseLesson>> getAllLessons() {
        return ResponseEntity.ok(courseLessonAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseLesson> getLessonById(@PathVariable String id) {
        return courseLessonAdminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLesson>> getLessonsByCourse(@PathVariable String courseId) {
        try {
            return ResponseEntity.ok(courseLessonAdminService.findByCourse(courseId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseLesson>> getLessonsByCategory(@PathVariable String categoryId) {
        try {
            return ResponseEntity.ok(courseLessonAdminService.findByCategory(categoryId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CourseLesson> createLesson(
            @RequestBody CourseLesson courseLesson,
            @RequestParam String courseId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String classroomId) {
        try {
            CourseLesson created = courseLessonAdminService.create(courseLesson, courseId, categoryId, classroomId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseLesson> updateLesson(
            @PathVariable String id,
            @RequestBody CourseLesson courseLesson,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String classroomId) {
        try {
            CourseLesson updated = courseLessonAdminService.update(id, courseLesson, courseId, categoryId, classroomId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable String id) {
        try {
            courseLessonAdminService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}