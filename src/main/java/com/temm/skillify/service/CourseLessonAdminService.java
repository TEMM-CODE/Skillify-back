package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.repository.CourseLessonRepository;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.ClassroomRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseLessonAdminService {
    
    private final CourseLessonRepository courseLessonRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final ClassroomRepository classroomRepository;
    
    public List<CourseLesson> findAll() {
        return courseLessonRepository.findAll();
    }
    
    public Optional<CourseLesson> findById(String id) {
        return courseLessonRepository.findById(id);
    }
    
    public List<CourseLesson> findByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseLessonRepository.findByCourse(course);
    }
    
    public List<CourseLesson> findByCategory(String categoryId) {
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
        return courseLessonRepository.findByCourseLessonCategory(category);
    }
    
    public CourseLesson create(CourseLesson courseLesson, String courseId, String categoryId, String classroomId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        courseLesson.setCourse(course);
        
        if (categoryId != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
            courseLesson.setCourseLessonCategory(category);
        }
        
        if (classroomId != null) {
            Classroom classroom = classroomRepository.findById(classroomId)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            courseLesson.setClassroom(classroom);
        }
        
        return courseLessonRepository.save(courseLesson);
    }
    
    public CourseLesson update(String id, CourseLesson updatedLesson, String courseId, 
                               String categoryId, String classroomId) {
        CourseLesson existingLesson = courseLessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson not found"));
        
        if (courseId != null) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            existingLesson.setCourse(course);
        }
        
        if (categoryId != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
            existingLesson.setCourseLessonCategory(category);
        }
        
        if (classroomId != null) {
            Classroom classroom = classroomRepository.findById(classroomId)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            existingLesson.setClassroom(classroom);
        }
        
        if (updatedLesson.getName() != null) {
            existingLesson.setName(updatedLesson.getName());
        }
        
        if (updatedLesson.getDuration() != null) {
            existingLesson.setDuration(updatedLesson.getDuration());
        }
        
        if (updatedLesson.getFiles() != null) {
            existingLesson.setFiles(updatedLesson.getFiles());
        }
        
        return courseLessonRepository.save(existingLesson);
    }
    
    public void deleteById(String id) {
        courseLessonRepository.deleteById(id);
    }
}