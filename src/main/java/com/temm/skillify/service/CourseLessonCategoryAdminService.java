package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.dto.request.CourseLessonCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.model.mapper.CourseLessonCategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonCategoryAdminService {
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonCategoryMapper courseLessonCategoryMapper;
    
    public List<CourseLessonCategoryResponseDTO> findAll() {
        return courseLessonCategoryRepository.findAll().stream()
            .map(courseLessonCategoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<CourseLessonCategoryResponseDTO> findById(String id) {
        return courseLessonCategoryRepository.findById(id)
            .map(courseLessonCategoryMapper::toResponseDTO);
    }
    
    public List<CourseLessonCategoryResponseDTO> findByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        
        return courseLessonCategoryRepository.findByCourse(course).stream()
            .map(courseLessonCategoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    public CourseLessonCategoryResponseDTO create(CourseLessonCategoryCreateDTO createDTO) {
        Course course = courseRepository.findById(createDTO.getCourseId())
            .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        
        CourseLessonCategory category = new CourseLessonCategory();
        category.setCourse(course);
        category.setName(createDTO.getName());
        
        CourseLessonCategory savedCategory = courseLessonCategoryRepository.save(category);
        return courseLessonCategoryMapper.toResponseDTO(savedCategory);
    }
    
    public CourseLessonCategoryResponseDTO update(String id, CourseLessonCategoryCreateDTO updateDTO) {
        CourseLessonCategory existingCategory = courseLessonCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
        
        if (updateDTO.getCourseId() != null) {
            Course course = courseRepository.findById(updateDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            existingCategory.setCourse(course);
        }
        
        if (updateDTO.getName() != null) {
            existingCategory.setName(updateDTO.getName());
        }
        
        CourseLessonCategory savedCategory = courseLessonCategoryRepository.save(existingCategory);
        return courseLessonCategoryMapper.toResponseDTO(savedCategory);
    }
    
    public void deleteById(String id) {
        courseLessonCategoryRepository.deleteById(id);
    }
}