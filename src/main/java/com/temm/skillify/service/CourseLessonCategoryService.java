package com.temm.skillify.service;

import com.temm.skillify.model.entity.Course;
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

@Service
public class CourseLessonCategoryService {

    @Autowired
    private CourseLessonCategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseLessonCategory> getCategoriesByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));
        
        // Verify the mentor has access to this course
        User currentUser = getCurrentUser();
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return categoryRepository.findByCourse(course);
    }

    public CourseLessonCategory getCategoryById(String id) {
        CourseLessonCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        
        // Verify the mentor has access to this category
        User currentUser = getCurrentUser();
        if (!category.getCourse().getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this category");
        }
        
        return category;
    }

    public CourseLessonCategory createCategory(CourseLessonCategory category) {
        // Verify mentor has access to the course
        User currentUser = getCurrentUser();
        Course course = courseRepository.findById(category.getCourse().getId())
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create categories for this course");
        }
        
        return categoryRepository.save(category);
    }

    public CourseLessonCategory updateCategory(String id, CourseLessonCategory updatedCategory) {
        CourseLessonCategory existingCategory = getCategoryById(id);
        
        // Cannot change the course of a category
        // Only update the name
        existingCategory.setName(updatedCategory.getName());
        
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        CourseLessonCategory category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}