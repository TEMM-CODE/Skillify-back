package com.temm.skillify.service;


import com.temm.skillify.model.dto.request.CourseCreateDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.CourseMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.CourseCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CourseCategoryRepository categoryRepository;
    
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassroomRepository classroomRepository;

public Set<CourseResponseDTO> getAllCoursesByCurrentMentor() {
    User currentUser = getCurrentUser();
    
    // Get all classrooms where current user is the mentor
    List<Classroom> classrooms = classroomRepository.findByMentor(currentUser);
    
    // Collect all courses from these classrooms into a set (to avoid duplicates)
    Set<Course> courses = classrooms.stream()
            .flatMap(classroom -> classroom.getCourses().stream())
            .collect(Collectors.toSet());
    
    return courses.stream()
            .map(courseMapper::toResponseDTO)
            .collect(Collectors.toSet());
}
    public CourseResponseDTO getCourseById(String id) {
        Course course = findCourseAndValidateAccessMentor(id);
        return courseMapper.toResponseDTO(course);
    }

    public CourseResponseDTO createCourse(CourseCreateDTO courseCreateDTO) {
        User currentUser = getCurrentUser();
        
        Course course = courseMapper.toEntity(courseCreateDTO);
        course.setCreator(currentUser);
        
        // Set categories if provided
        if (courseCreateDTO.getCategoryIds() != null && !courseCreateDTO.getCategoryIds().isEmpty()) {
            Set<CourseCategory> categories = courseCreateDTO.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
            course.setCategories(categories);
        }
        
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(savedCourse);
    }

    public CourseResponseDTO updateCourse(String id, CourseCreateDTO courseUpdateDTO) {
        Course existingCourse = findCourseAndValidateAccessMentor(id);
        
        // Update course fields from DTO
        courseMapper.updateEntityFromDTO(existingCourse, courseUpdateDTO);
        
        // Update categories if provided
        if (courseUpdateDTO.getCategoryIds() != null) {
            Set<CourseCategory> categories = courseUpdateDTO.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
            existingCourse.setCategories(categories);
        }
        
        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toResponseDTO(updatedCourse);
    }

    public void deleteCourse(String id) {
        Course course = findCourseAndValidateAccess(id);
        courseRepository.delete(course);
    }

    private Course findCourseAndValidateAccess(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
        
        User currentUser = getCurrentUser();
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return course;
    }

    private Course findCourseAndValidateAccessMentor(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));
        
        User currentUser = getCurrentUser();
        
        // Check if the course exists in any of the mentor's classrooms
        boolean hasAccess = classroomRepository.findByMentor(currentUser).stream()
                .flatMap(classroom -> classroom.getCourses().stream())
                .anyMatch(c -> c.getId().equals(courseId));
        
        if (!hasAccess) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return course;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}