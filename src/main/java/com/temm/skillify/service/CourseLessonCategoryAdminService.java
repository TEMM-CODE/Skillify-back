package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseLessonCategoryAdminService {
    
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final CourseRepository courseRepository;
    
    public List<CourseLessonCategory> findAll() {
        return courseLessonCategoryRepository.findAll();
    }
    
    public Optional<CourseLessonCategory> findById(String id) {
        return courseLessonCategoryRepository.findById(id);
    }
    
    public List<CourseLessonCategory> findByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseLessonCategoryRepository.findByCourse(course);
    }
    
    public CourseLessonCategory create(CourseLessonCategory category, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        category.setCourse(course);
        return courseLessonCategoryRepository.save(category);
    }
    
    public CourseLessonCategory update(String id, CourseLessonCategory updatedCategory, String courseId) {
        CourseLessonCategory existingCategory = courseLessonCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
        
        if (courseId != null) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            existingCategory.setCourse(course);
        }
        
        if (updatedCategory.getName() != null) {
            existingCategory.setName(updatedCategory.getName());
        }
        
        return courseLessonCategoryRepository.save(existingCategory);
    }
    
    public void deleteById(String id) {
        courseLessonCategoryRepository.deleteById(id);
    }
}