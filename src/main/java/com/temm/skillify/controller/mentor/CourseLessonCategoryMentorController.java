package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.CourseLessonCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.service.CourseLessonCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/course-categories")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseLessonCategoryMentorController {

    @Autowired
    private CourseLessonCategoryService categoryService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonCategoryResponseDTO>> getCategoriesByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(categoryService.getCategoriesByCourse(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonCategoryResponseDTO> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CourseLessonCategoryResponseDTO> createCategory(@RequestBody CourseLessonCategoryCreateDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonCategoryResponseDTO> updateCategory(
            @PathVariable String id, 
            @RequestBody CourseLessonCategoryCreateDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}