package com.temm.skillify.service;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.repository.CourseCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseAdminService {
    
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> findCourseById(String id) {
        return courseRepository.findById(id);
    }
    
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public Course updateCourse(String id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setLevel(updatedCourse.getLevel());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setImageUrl(updatedCourse.getImageUrl());
        
        if (updatedCourse.getCategories() != null && !updatedCourse.getCategories().isEmpty()) {
            existingCourse.setCategories(updatedCourse.getCategories());
        }
        
        if (updatedCourse.getCreator() != null) {
            existingCourse.setCreator(updatedCourse.getCreator());
        }
        
        return courseRepository.save(existingCourse);
    }
    
    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }
    
    public List<CourseCategory> findAllCategories() {
        return courseCategoryRepository.findAll();
    }
    
    public CourseCategory createCategory(CourseCategory category) {
        return courseCategoryRepository.save(category);
    }
    
    public void deleteCategory(String id) {
        courseCategoryRepository.deleteById(id);
    }
    
    public Course updateCourseCategories(String courseId, Set<CourseCategory> categories) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        course.setCategories(categories);
        return courseRepository.save(course);
    }
}