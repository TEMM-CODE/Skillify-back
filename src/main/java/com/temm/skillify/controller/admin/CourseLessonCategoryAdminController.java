package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.request.CourseLessonCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.service.CourseLessonCategoryAdminService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/course-lesson-categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CourseLessonCategoryAdminController {
    private final CourseLessonCategoryAdminService courseLessonCategoryAdminService;
    
    @GetMapping
    public ResponseEntity<List<CourseLessonCategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(courseLessonCategoryAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonCategoryResponseDTO> getCategoryById(@PathVariable String id) {
        return courseLessonCategoryAdminService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonCategoryResponseDTO>> getCategoriesByCourse(@PathVariable String courseId) {
        try {
            return ResponseEntity.ok(courseLessonCategoryAdminService.findByCourse(courseId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CourseLessonCategoryResponseDTO> createCategory(
            @RequestBody CourseLessonCategoryCreateDTO createDTO) {
        try {
            CourseLessonCategoryResponseDTO created = courseLessonCategoryAdminService.create(createDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonCategoryResponseDTO> updateCategory(
            @PathVariable String id,
            @RequestBody CourseLessonCategoryCreateDTO updateDTO) {
        try {
            CourseLessonCategoryResponseDTO updated = courseLessonCategoryAdminService.update(id, updateDTO);
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