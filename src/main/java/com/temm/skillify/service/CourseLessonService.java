package com.temm.skillify.service;


import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.CourseLessonRepository;
import com.temm.skillify.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CourseLessonService {

    @Autowired
    private CourseLessonRepository courseLessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseLessonCategoryRepository courseLessonCategoryRepository;

    public List<CourseLesson> getAllLessonsByMentor() {
        User currentUser = getCurrentUser();
        
        // Filter lessons by courses created by the current mentor
        List<Course> mentorCourses = courseRepository.findByCreator(currentUser);
        return mentorCourses.stream()
                .flatMap(course -> courseLessonRepository.findByCourse(course).stream())
                .collect(Collectors.toList());
    }

    public List<CourseLesson> getLessonsByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));
        
        // Verify the mentor has access to this course
        User currentUser = getCurrentUser();
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return courseLessonRepository.findByCourse(course);
    }

    public List<CourseLesson> getLessonsByCategory(String categoryId) {
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + categoryId));
        
        // Verify the mentor has access to the course of this category
        User currentUser = getCurrentUser();
        if (!category.getCourse().getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this category");
        }
        
        return courseLessonRepository.findByCourseLessonCategory(category);
    }

    public CourseLesson getLessonById(String id) {
        CourseLesson lesson = courseLessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found with id: " + id));
        
        // Verify the mentor has access to this lesson
        User currentUser = getCurrentUser();
        if (!lesson.getCourse().getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this lesson");
        }
        
        return lesson;
    }

    public CourseLesson createLesson(CourseLesson lesson) {
        // Verify mentor has access to the course
        User currentUser = getCurrentUser();
        Course course = courseRepository.findById(lesson.getCourse().getId())
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create lessons for this course");
        }
        
        // If a category is provided, verify access
        if (lesson.getCourseLessonCategory() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(lesson.getCourseLessonCategory().getId())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
            
            if (!category.getCourse().getId().equals(course.getId())) {
                throw new IllegalArgumentException("Category does not belong to the specified course");
            }
        }
        
        return courseLessonRepository.save(lesson);
    }

    public CourseLesson updateLesson(String id, CourseLesson updatedLesson) {
        CourseLesson existingLesson = getLessonById(id);
        
        // Update fields
        existingLesson.setName(updatedLesson.getName());
        existingLesson.setDuration(updatedLesson.getDuration());
        existingLesson.setFiles(updatedLesson.getFiles());
        
        // If category is being updated, verify access
        if (updatedLesson.getCourseLessonCategory() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(updatedLesson.getCourseLessonCategory().getId())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
            
            if (!category.getCourse().getId().equals(existingLesson.getCourse().getId())) {
                throw new IllegalArgumentException("Category does not belong to the lesson's course");
            }
            
            existingLesson.setCourseLessonCategory(category);
        }
        
        // If classroom is being updated
        if (updatedLesson.getClassroom() != null) {
            existingLesson.setClassroom(updatedLesson.getClassroom());
        }
        
        return courseLessonRepository.save(existingLesson);
    }

    public void deleteLesson(String id) {
        CourseLesson lesson = getLessonById(id);
        courseLessonRepository.delete(lesson);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
