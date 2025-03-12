package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.CourseMapper;
import com.temm.skillify.repository.CourseCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseAdminService {
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public List<CourseResponseDTO> findAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<CourseResponseDTO> findCourseById(String id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponseDTO);
    }

    public CourseResponseDTO createCourse(CourseCreateDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        
        // Set creator
        if (courseDTO.getCreatorId() != null) {
            User creator = userRepository.findById(courseDTO.getCreatorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + courseDTO.getCreatorId()));
            course.setCreator(creator);
        }
        
        // Set categories
        if (courseDTO.getCategoryIds() != null && !courseDTO.getCategoryIds().isEmpty()) {
            Set<CourseCategory> categories = courseDTO.getCategoryIds().stream()
                    .map(categoryId -> courseCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
            course.setCategories(categories);
        }
        
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(savedCourse);
    }

    public CourseResponseDTO updateCourse(String id, CourseCreateDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        
        courseMapper.updateEntityFromDTO(existingCourse, courseDTO);
        
        // Update creator if provided
        if (courseDTO.getCreatorId() != null) {
            User creator = userRepository.findById(courseDTO.getCreatorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + courseDTO.getCreatorId()));
            existingCourse.setCreator(creator);
        }
        
        // Update categories if provided
        if (courseDTO.getCategoryIds() != null && !courseDTO.getCategoryIds().isEmpty()) {
            Set<CourseCategory> categories = courseDTO.getCategoryIds().stream()
                    .map(categoryId -> courseCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
            existingCourse.setCategories(categories);
        }
        
        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toResponseDTO(updatedCourse);
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    public List<CourseCategoryResponseDTO> findAllCategories() {
        return courseCategoryRepository.findAll().stream()
                .map(this::mapCategoryToDTO)
                .collect(Collectors.toList());
    }

    public CourseCategoryResponseDTO createCategory(CourseCategory category) {
        CourseCategory savedCategory = courseCategoryRepository.save(category);
        return mapCategoryToDTO(savedCategory);
    }

    public void deleteCategory(String id) {
        courseCategoryRepository.deleteById(id);
    }

    public CourseResponseDTO updateCourseCategories(String courseId, Set<String> categoryIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        Set<CourseCategory> categories = new HashSet<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            categories = categoryIds.stream()
                    .map(categoryId -> courseCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
        }
        
        course.setCategories(categories);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(updatedCourse);
    }
    
    private CourseCategoryResponseDTO mapCategoryToDTO(CourseCategory category) {
        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}