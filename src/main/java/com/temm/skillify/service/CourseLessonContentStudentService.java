package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.CourseLessonContent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.CourseLessonContentMapper;
import com.temm.skillify.repository.CourseLessonContentRepository;
import com.temm.skillify.repository.ClassroomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonContentStudentService {
    
    private final CourseLessonContentRepository courseLessonContentRepository;
    private final ClassroomRepository classroomRepository;
    private final CourseLessonContentMapper contentMapper;
    private final UserService userService;

    public List<CourseLessonContentResponseDTO> getAllCourseLessonContentsForStudent(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // 1. Get all classrooms the student is enrolled in
        List<String> classroomIds = classroomRepository.findByStudentsContaining(student)
            .stream()
            .map(Classroom::getId)
            .collect(Collectors.toList());
        
        // 2. Get all course IDs from those classrooms
        Set<String> courseIds = classroomRepository.findByStudentsContaining(student)
            .stream()
            .flatMap(classroom -> classroom.getCourses().stream())
            .map(course -> course.getId())
            .collect(Collectors.toSet());
        
        // 3. Get all content for the student's enrolled courses
        return courseLessonContentRepository.findAll()
            .stream()
            .filter(content -> courseIds.contains(content.getCourseLesson().getCourse().getId()))
            .map(contentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    public CourseLessonContentResponseDTO getCourseLessonContentById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        CourseLessonContent content = courseLessonContentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course lesson content not found with id: " + id));
        
        // Verify access through classroom enrollment
        if (!isStudentEnrolledInCourse(content.getCourseLesson().getCourse().getId(), student)) {
            throw new EntityNotFoundException("Course lesson content not found or not accessible");
        }
        
        return contentMapper.toResponseDTO(content);
    }
    
    public List<CourseLessonContentResponseDTO> getCourseLessonContentsByLessonId(String lessonId, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        List<CourseLessonContent> contents = courseLessonContentRepository.findByCourseLessonIdOrderByPositionAsc(lessonId);
        
        if (contents.isEmpty()) {
            throw new EntityNotFoundException("No content found for lesson with id: " + lessonId);
        }
        
        String courseId = contents.get(0).getCourseLesson().getCourse().getId();
        if (!isStudentEnrolledInCourse(courseId, student)) {
            throw new EntityNotFoundException("Lesson content not found or not accessible");
        }
        
        return contents.stream()
            .map(contentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    private boolean isStudentEnrolledInCourse(String courseId, User student) {
        return classroomRepository.findByStudentsContaining(student)
            .stream()
            .flatMap(classroom -> classroom.getCourses().stream())
            .anyMatch(course -> course.getId().equals(courseId));
    }
}