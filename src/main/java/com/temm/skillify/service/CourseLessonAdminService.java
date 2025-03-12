package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.CourseLessonCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.mapper.CourseLessonMapper;
import com.temm.skillify.repository.CourseLessonRepository;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.ClassroomRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonAdminService {
    
    private final CourseLessonRepository courseLessonRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final ClassroomRepository classroomRepository;
    private final CourseLessonMapper courseLessonMapper;
    
    public List<CourseLessonResponseDTO> findAll() {
        return courseLessonRepository.findAll().stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<CourseLessonResponseDTO> findById(String id) {
        return courseLessonRepository.findById(id)
                .map(courseLessonMapper::toResponseDTO);
    }
    
    public List<CourseLessonResponseDTO> findByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseLessonRepository.findByCourse(course).stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<CourseLessonResponseDTO> findByCategory(String categoryId) {
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
        return courseLessonRepository.findByCourseLessonCategory(category).stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public CourseLessonResponseDTO create(CourseLessonCreateDTO createDTO) {
        CourseLesson courseLesson = new CourseLesson();
        courseLesson.setName(createDTO.getName());
        courseLesson.setDuration(createDTO.getDuration());
        courseLesson.setFiles(createDTO.getFiles());
        
        Course course = courseRepository.findById(createDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        courseLesson.setCourse(course);
        
        if (createDTO.getCourseLessonCategoryId() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(createDTO.getCourseLessonCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
            courseLesson.setCourseLessonCategory(category);
        }
        
        if (createDTO.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(createDTO.getClassroomId())
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            courseLesson.setClassroom(classroom);
        }
        
        CourseLesson savedLesson = courseLessonRepository.save(courseLesson);
        return courseLessonMapper.toResponseDTO(savedLesson);
    }
    
    public CourseLessonResponseDTO update(String id, CourseLessonCreateDTO updateDTO) {
        CourseLesson existingLesson = courseLessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson not found"));
        
        if (updateDTO.getCourseId() != null) {
            Course course = courseRepository.findById(updateDTO.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            existingLesson.setCourse(course);
        }
        
        if (updateDTO.getCourseLessonCategoryId() != null) {
            CourseLessonCategory category = courseLessonCategoryRepository.findById(updateDTO.getCourseLessonCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found"));
            existingLesson.setCourseLessonCategory(category);
        }
        
        if (updateDTO.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(updateDTO.getClassroomId())
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            existingLesson.setClassroom(classroom);
        }
        
        if (updateDTO.getName() != null) {
            existingLesson.setName(updateDTO.getName());
        }
        
        if (updateDTO.getDuration() != null) {
            existingLesson.setDuration(updateDTO.getDuration());
        }
        
        if (updateDTO.getFiles() != null) {
            existingLesson.setFiles(updateDTO.getFiles());
        }
        
        CourseLesson updatedLesson = courseLessonRepository.save(existingLesson);
        return courseLessonMapper.toResponseDTO(updatedLesson);
    }
    
    public void deleteById(String id) {
        courseLessonRepository.deleteById(id);
    }
}