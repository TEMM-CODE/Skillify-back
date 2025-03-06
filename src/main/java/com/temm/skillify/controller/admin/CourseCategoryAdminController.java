package com.temm.skillify.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.service.CourseCategoryAdminService;
import com.temm.skillify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/course-categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CourseCategoryAdminController {
    
    private final CourseCategoryAdminService courseCategoryService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<CourseCategory>> getAllCategories() {
        return ResponseEntity.ok(courseCategoryService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseCategory> getCategoryById(@PathVariable String id) {
        return courseCategoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CourseCategory> createCategory(@RequestBody CourseCategory courseCategory, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        CourseCategory savedCategory = courseCategoryService.save(courseCategory);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseCategory> updateCategory(
            @PathVariable String id,
            @RequestBody CourseCategory courseCategory,
            Authentication authentication) {
        
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        return courseCategoryService.findById(id)
                .map(existingCategory -> {
                    existingCategory.setCategoryName(courseCategory.getCategoryName());
                    CourseCategory updatedCategory = courseCategoryService.save(existingCategory);
                    return ResponseEntity.ok(updatedCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id, Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        
        if (courseCategoryService.findById(id).isPresent()) {
            courseCategoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}