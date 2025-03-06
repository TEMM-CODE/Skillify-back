package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.service.CourseLessonCategoryAdminService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/course-lesson-categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CourseLessonCategoryAdminController {
    
    private final CourseLessonCategoryAdminService courseLessonCategoryAdminService;
    
    @GetMapping
    public ResponseEntity<List<CourseLessonCategory>> getAllCategories() {
        return ResponseEntity.ok(courseLessonCategoryAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonCategory> getCategoryById(@PathVariable String id) {
        return courseLessonCategoryAdminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonCategory>> getCategoriesByCourse(@PathVariable String courseId) {
        try {
            return ResponseEntity.ok(courseLessonCategoryAdminService.findByCourse(courseId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CourseLessonCategory> createCategory(
            @RequestBody CourseLessonCategory category,
            @RequestParam String courseId) {
        try {
            CourseLessonCategory created = courseLessonCategoryAdminService.create(category, courseId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonCategory> updateCategory(
            @PathVariable String id,
            @RequestBody CourseLessonCategory category,
            @RequestParam(required = false) String courseId) {
        try {
            CourseLessonCategory updated = courseLessonCategoryAdminService.update(id, category, courseId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        try {
            courseLessonCategoryAdminService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}