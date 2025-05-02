package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.*;
import com.temm.skillify.model.entity.*;
import com.temm.skillify.model.mapper.CourseLessonContentMapper;
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
    private final CourseLessonContentMapper courseLessonContentMapper;

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

    private CourseLessonResponseDTO mapToCourseLessonResponseDTO(CourseLesson entity) {
            if (entity == null) {
                return null;
            }
            
            CourseLessonResponseDTO dto = new CourseLessonResponseDTO();
            
            // Set base fields
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setDuration(entity.getDuration());
            dto.setFiles(entity.getFiles());
            
            // Set course if available
            if (entity.getCourse() != null) {
                CourseResponseDTO courseDTO = new CourseResponseDTO();
                courseDTO.setId(entity.getCourse().getId());
                courseDTO.setName(entity.getCourse().getName());
                courseDTO.setDescription(entity.getCourse().getDescription());
                courseDTO.setLevel(entity.getCourse().getLevel());
                courseDTO.setDuration(entity.getCourse().getDuration());
                courseDTO.setImageUrl(entity.getCourse().getImageUrl());
                
                // Set creator if available
                if (entity.getCourse().getCreator() != null) {
                    UserResponseDTO creatorDTO = new UserResponseDTO();
                    creatorDTO.setId(entity.getCourse().getCreator().getId());
                    creatorDTO.setName(entity.getCourse().getCreator().getName());
                    creatorDTO.setEmail(entity.getCourse().getCreator().getEmail());
                    creatorDTO.setRole(entity.getCourse().getCreator().getRole());
                    
                    courseDTO.setCreator(creatorDTO);
                }
                
                // Set categories if available
                if (entity.getCourse().getCategories() != null) {
                    courseDTO.setCategories(entity.getCourse().getCategories().stream()
                            .map(category -> {
                                CourseCategoryResponseDTO categoryDTO = new CourseCategoryResponseDTO();
                                categoryDTO.setId(category.getId());
                                categoryDTO.setCategoryName(category.getCategoryName());
                                return categoryDTO;
                            })
                            .collect(Collectors.toSet()));
                }
                
                dto.setCourse(courseDTO);
            }
            
            // Set course lesson category if available
            if (entity.getCourseLessonCategory() != null) {
                CourseLessonCategoryResponseDTO categoryDTO = new CourseLessonCategoryResponseDTO();
                categoryDTO.setId(entity.getCourseLessonCategory().getId());
                categoryDTO.setName(entity.getCourseLessonCategory().getName());
                
                if (entity.getCourseLessonCategory().getCourse() != null) {
                    CourseResponseDTO simpleCourseDTO = new CourseResponseDTO();
                    simpleCourseDTO.setId(entity.getCourseLessonCategory().getCourse().getId());
                    simpleCourseDTO.setName(entity.getCourseLessonCategory().getCourse().getName());
                    categoryDTO.setCourse(simpleCourseDTO);
                }
                
                dto.setCourseLessonCategory(categoryDTO);
            }
            
            // Set content list if available
            if (entity.getContent() != null && !entity.getContent().isEmpty()) {
                dto.setContent(entity.getContent().stream()
                    .map(courseLessonContentMapper::toResponseDTO)
                    .collect(Collectors.toList()));
            }
            
            return dto;
        }
}