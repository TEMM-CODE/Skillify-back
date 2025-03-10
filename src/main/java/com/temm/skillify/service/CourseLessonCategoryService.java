package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseLessonCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CourseLessonCategoryService {

    @Autowired
    private CourseLessonCategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseLessonCategoryResponseDTO> getCategoriesByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));
        
        // Verify the mentor has access to this course
        User currentUser = getCurrentUser();
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        List<CourseLessonCategory> categories = categoryRepository.findByCourse(course);
        return categories.stream()
                .map(this::toCourseLessonCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseLessonCategoryResponseDTO getCategoryById(String id) {
        CourseLessonCategory category = findCategoryAndVerifyAccess(id);
        return toCourseLessonCategoryResponseDTO(category);
    }

    public CourseLessonCategoryResponseDTO createCategory(CourseLessonCategoryCreateDTO categoryDTO) {
        // Verify mentor has access to the course
        User currentUser = getCurrentUser();
        Course course = courseRepository.findById(categoryDTO.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
                
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create categories for this course");
        }
        
        // Create new category entity
        CourseLessonCategory category = new CourseLessonCategory();
        category.setCourse(course);
        category.setName(categoryDTO.getName());
        
        CourseLessonCategory savedCategory = categoryRepository.save(category);
        return toCourseLessonCategoryResponseDTO(savedCategory);
    }

    public CourseLessonCategoryResponseDTO updateCategory(String id, CourseLessonCategoryCreateDTO categoryDTO) {
        CourseLessonCategory existingCategory = findCategoryAndVerifyAccess(id);
        
        // Only update the name
        existingCategory.setName(categoryDTO.getName());
        
        CourseLessonCategory updatedCategory = categoryRepository.save(existingCategory);
        return toCourseLessonCategoryResponseDTO(updatedCategory);
    }

    public void deleteCategory(String id) {
        CourseLessonCategory category = findCategoryAndVerifyAccess(id);
        categoryRepository.delete(category);
    }

    private CourseLessonCategory findCategoryAndVerifyAccess(String id) {
        CourseLessonCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
                
        // Verify the mentor has access to this category
        User currentUser = getCurrentUser();
        if (!category.getCourse().getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this category");
        }
        
        return category;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    
    // DTO Mapping methods
    
    private CourseLessonCategoryResponseDTO toCourseLessonCategoryResponseDTO(CourseLessonCategory category) {
        CourseLessonCategoryResponseDTO dto = new CourseLessonCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCourse(toCourseResponseDTO(category.getCourse()));
        // Set other base fields if needed
        return dto;
    }
    
    private CourseResponseDTO toCourseResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        dto.setCreator(toUserResponseDTO(course.getCreator()));
        
        // Convert course categories if present
        if (course.getCategories() != null) {
            dto.setCategories(course.getCategories().stream()
                    .map(this::toCourseCategoryResponseDTO)
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
    
    private CourseCategoryResponseDTO toCourseCategoryResponseDTO(CourseCategory category) {
        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }
    
    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        // Set other user fields based on your User entity and UserResponseDTO
        return dto;
    }
}