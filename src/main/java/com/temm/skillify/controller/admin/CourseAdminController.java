package com.temm.skillify.controller.admin;

import com.temm.skillify.model.entity.Course;
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
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CourseAdminController {
    
    private final CourseAdminService courseAdminService;
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseAdminService.findAllCourses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return courseAdminService.findCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseAdminService.createCourse(course));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course course) {
        return ResponseEntity.ok(courseAdminService.updateCourse(id, course));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseAdminService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<CourseCategory>> getAllCategories() {
        return ResponseEntity.ok(courseAdminService.findAllCategories());
    }
    
    @PostMapping("/categories")
    public ResponseEntity<CourseCategory> createCategory(@RequestBody CourseCategory category) {
        return ResponseEntity.ok(courseAdminService.createCategory(category));
    }
    
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        courseAdminService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{courseId}/categories")
    public ResponseEntity<Course> updateCourseCategories(
            @PathVariable String courseId, 
            @RequestBody Set<CourseCategory> categories) {
        return ResponseEntity.ok(courseAdminService.updateCourseCategories(courseId, categories));
    }
}