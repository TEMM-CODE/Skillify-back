package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseLessonCreateDTO;
import com.temm.skillify.model.entity.*;
import com.temm.skillify.repository.*;
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
    
    @Autowired
    private ClassroomRepository classroomRepository;

    public List<CourseLesson> getAllLessonsByMentor() {
        User currentUser = getCurrentUser();
        
        // Get all classrooms for the mentor
        List<Classroom> mentorClassrooms = classroomRepository.findByMentor(currentUser);
        
        // Get all courses from these classrooms
        List<Course> mentorCourses = mentorClassrooms.stream()
                .flatMap(classroom -> classroom.getCourses().stream())
                .collect(Collectors.toList());
                
        // Get all lessons from these courses
        return mentorCourses.stream()
                .flatMap(course -> courseLessonRepository.findByCourse(course).stream())
                .collect(Collectors.toList());
    }

    public List<CourseLesson> getLessonsByCourse(String courseId) {
        User currentUser = getCurrentUser();
        
        // Get course and verify it belongs to one of the mentor's classrooms
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));
        
        boolean isCourseInMentorClassroom = classroomRepository.findByMentor(currentUser).stream()
                .flatMap(classroom -> classroom.getCourses().stream())
                .anyMatch(c -> c.getId().equals(courseId));
                
        if (!isCourseInMentorClassroom) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return courseLessonRepository.findByCourse(course);
    }

    public List<CourseLesson> getLessonsByCategory(String categoryId) {
        User currentUser = getCurrentUser();
        
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + categoryId));
        
        // Verify the category's course belongs to one of the mentor's classrooms
        boolean isCourseInMentorClassroom = classroomRepository.findByMentor(currentUser).stream()
                .flatMap(classroom -> classroom.getCourses().stream())
                .anyMatch(c -> c.getId().equals(category.getCourse().getId()));
                
        if (!isCourseInMentorClassroom) {
            throw new SecurityException("You don't have permission to access this category");
        }
        
        return courseLessonRepository.findByCourseLessonCategory(category);
    }

    public CourseLesson getLessonById(String id) {
        User currentUser = getCurrentUser();
        
        CourseLesson lesson = courseLessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found with id: " + id));
        
        // Verify the lesson's course belongs to one of the mentor's classrooms
        boolean isCourseInMentorClassroom = classroomRepository.findByMentor(currentUser).stream()
                .flatMap(classroom -> classroom.getCourses().stream())
                .anyMatch(c -> c.getId().equals(lesson.getCourse().getId()));
                
        if (!isCourseInMentorClassroom) {
            throw new SecurityException("You don't have permission to access this lesson");
        }
        
        return lesson;
    }

    public CourseLesson createLesson(CourseLessonCreateDTO createDTO) {
        // Verify mentor has access to the course
        User currentUser = getCurrentUser();
        Course course = courseRepository.findById(createDTO.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        
       /*  if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create lessons for this course");
        } */
        
        // Create new lesson
        CourseLesson lesson = new CourseLesson();
        lesson.setCourse(course);
        lesson.setName(createDTO.getName());
        lesson.setDuration(createDTO.getDuration());
        lesson.setFiles(createDTO.getFiles());
        
        // If a category is provided, verify access and set it
        if (createDTO.getCourseLessonCategoryId() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(createDTO.getCourseLessonCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
            
            if (!category.getCourse().getId().equals(course.getId())) {
                throw new IllegalArgumentException("Category does not belong to the specified course");
            }
            
            lesson.setCourseLessonCategory(category);
        }
        
        
        return courseLessonRepository.save(lesson);
    }

    public CourseLesson updateLesson(String id, CourseLessonCreateDTO updateDTO) {
        CourseLesson existingLesson = getLessonById(id);
        
        // Update basic fields
        existingLesson.setName(updateDTO.getName());
        existingLesson.setDuration(updateDTO.getDuration());
        existingLesson.setFiles(updateDTO.getFiles());
        
        // If category is being updated, verify access
        if (updateDTO.getCourseLessonCategoryId() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(updateDTO.getCourseLessonCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
            
            if (!category.getCourse().getId().equals(existingLesson.getCourse().getId())) {
                throw new IllegalArgumentException("Category does not belong to the lesson's course");
            }
            
            existingLesson.setCourseLessonCategory(category);
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