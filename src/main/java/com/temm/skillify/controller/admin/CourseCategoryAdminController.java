package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.request.CourseCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
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
    public ResponseEntity<List<CourseCategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(courseCategoryService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseCategoryResponseDTO> getCategoryById(@PathVariable String id) {
        return courseCategoryService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CourseCategoryResponseDTO> createCategory(
            @RequestBody CourseCategoryCreateDTO categoryDTO, 
            Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        CourseCategoryResponseDTO savedCategory = courseCategoryService.save(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseCategoryResponseDTO> updateCategory(
            @PathVariable String id,
            @RequestBody CourseCategoryCreateDTO categoryDTO,
            Authentication authentication) {
        userService.getUserFromAuthentication(authentication); // Verify admin is authenticated
        return courseCategoryService.update(id, categoryDTO)
            .map(ResponseEntity::ok)
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