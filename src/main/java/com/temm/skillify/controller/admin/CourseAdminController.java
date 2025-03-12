package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.CourseCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.service.CourseAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class CourseAdminController {
    private final CourseAdminService courseAdminService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseAdminService.findAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable String id) {
        return courseAdminService.findCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseCreateDTO courseDTO) {
        return ResponseEntity.ok(courseAdminService.createCourse(courseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable String id, @RequestBody CourseCreateDTO courseDTO) {
        return ResponseEntity.ok(courseAdminService.updateCourse(id, courseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseAdminService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CourseCategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(courseAdminService.findAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<CourseCategoryResponseDTO> createCategory(@RequestBody CourseCategory category) {
        return ResponseEntity.ok(courseAdminService.createCategory(category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        courseAdminService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}/categories")
    public ResponseEntity<CourseResponseDTO> updateCourseCategories(
            @PathVariable String courseId,
            @RequestBody Set<String> categoryIds) {
        return ResponseEntity.ok(courseAdminService.updateCourseCategories(courseId, categoryIds));
    }
}