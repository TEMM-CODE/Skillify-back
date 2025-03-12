package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.CourseLessonCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
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
    public ResponseEntity<List<CourseLessonResponseDTO>> getAllLessons() {
        return ResponseEntity.ok(courseLessonAdminService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonResponseDTO> getLessonById(@PathVariable String id) {
        return courseLessonAdminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonResponseDTO>> getLessonsByCourse(@PathVariable String courseId) {
        try {
            return ResponseEntity.ok(courseLessonAdminService.findByCourse(courseId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseLessonResponseDTO>> getLessonsByCategory(@PathVariable String categoryId) {
        try {
            return ResponseEntity.ok(courseLessonAdminService.findByCategory(categoryId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CourseLessonResponseDTO> createLesson(@RequestBody CourseLessonCreateDTO createDTO) {
        try {
            CourseLessonResponseDTO created = courseLessonAdminService.create(createDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonResponseDTO> updateLesson(
            @PathVariable String id,
            @RequestBody CourseLessonCreateDTO updateDTO) {
        try {
            CourseLessonResponseDTO updated = courseLessonAdminService.update(id, updateDTO);
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