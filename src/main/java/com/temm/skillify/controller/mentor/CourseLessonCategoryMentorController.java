package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.service.CourseLessonCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/course-categories")
@PreAuthorize("hasRole('MENTOR')")
public class CourseLessonCategoryMentorController {

    @Autowired
    private CourseLessonCategoryService categoryService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonCategory>> getCategoriesByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(categoryService.getCategoriesByCourse(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonCategory> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CourseLessonCategory> createCategory(@RequestBody CourseLessonCategory category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonCategory> updateCategory(@PathVariable String id, @RequestBody CourseLessonCategory category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}