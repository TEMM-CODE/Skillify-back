package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.*;
import com.temm.skillify.model.entity.*;
import com.temm.skillify.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final CourseRepository courseRepository;
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final CourseLessonRepository courseLessonRepository;
    private final UserService userService;
    private final ClassroomRepository classroomRepository;

    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseResponseDTO getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        return mapToCourseResponseDTO(course);
    }


    public List<CourseLessonCategoryResponseDTO> getCourseLessonCategories(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        List<CourseLessonCategory> categories = courseLessonCategoryRepository.findByCourse(course);
        return categories.stream()
                .map(this::mapToCourseLessonCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseLessonResponseDTO> getCourseLessons(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        List<CourseLesson> lessons = courseLessonRepository.findByCourse(course);
        return lessons.stream()
                .map(this::mapToCourseLessonResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseLessonResponseDTO> getLessonsByCategory(String categoryId) {
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found with id: " + categoryId));
        
        List<CourseLesson> lessons = courseLessonRepository.findByCourseLessonCategory(category);
        return lessons.stream()
                .map(this::mapToCourseLessonResponseDTO)
                .collect(Collectors.toList());
    }



    private CourseResponseDTO mapToCourseResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        
        // Map creator information
        UserResponseDTO creatorDTO = new UserResponseDTO();
        creatorDTO.setId(course.getCreator().getId());
        creatorDTO.setName(course.getCreator().getName());
        creatorDTO.setEmail(course.getCreator().getEmail());
        dto.setCreator(creatorDTO);
        
        // Map categories
        Set<CourseCategoryResponseDTO> categoryDTOs = course.getCategories().stream()
                .map(category -> {
                    CourseCategoryResponseDTO categoryDTO = new CourseCategoryResponseDTO();
                    categoryDTO.setId(category.getId());
                    categoryDTO.setCategoryName(category.getCategoryName());
                    categoryDTO.setCreatedAt(category.getCreatedAt());
                    categoryDTO.setUpdatedAt(category.getUpdatedAt());
                    return categoryDTO;
                })
                .collect(Collectors.toSet());
        
        dto.setCategories(categoryDTOs);
        
        return dto;
    }

    private CourseLessonCategoryResponseDTO mapToCourseLessonCategoryResponseDTO(CourseLessonCategory category) {
        CourseLessonCategoryResponseDTO dto = new CourseLessonCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        courseDTO.setId(category.getCourse().getId());
        courseDTO.setName(category.getCourse().getName());
        dto.setCourse(courseDTO);
        
        return dto;
    }

    private CourseLessonResponseDTO mapToCourseLessonResponseDTO(CourseLesson lesson) {
        CourseLessonResponseDTO dto = new CourseLessonResponseDTO();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDuration(lesson.getDuration());
        dto.setFiles(lesson.getFiles());
        dto.setCreatedAt(lesson.getCreatedAt());
        dto.setUpdatedAt(lesson.getUpdatedAt());
        
        // Map course information
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        courseDTO.setId(lesson.getCourse().getId());
        courseDTO.setName(lesson.getCourse().getName());
        dto.setCourse(courseDTO);
        
        // Map category information if available
        if (lesson.getCourseLessonCategory() != null) {
            CourseLessonCategoryResponseDTO categoryDTO = new CourseLessonCategoryResponseDTO();
            categoryDTO.setId(lesson.getCourseLessonCategory().getId());
            categoryDTO.setName(lesson.getCourseLessonCategory().getName());
            dto.setCourseLessonCategory(categoryDTO);
        }
        
        // Map classroom information if available

        
        return dto;
    }
}