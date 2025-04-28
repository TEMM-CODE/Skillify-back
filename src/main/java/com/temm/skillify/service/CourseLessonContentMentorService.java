package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseLessonContentCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.entity.CourseLessonContent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.CourseLessonContentMapper;
import com.temm.skillify.repository.CourseLessonContentRepository;
import com.temm.skillify.repository.CourseLessonRepository;
import com.temm.skillify.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonContentMentorService {
    
    private final CourseLessonContentRepository courseLessonContentRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonContentMapper courseLessonContentMapper;
    private final CourseLessonRepository courseLessonRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public List<CourseLessonContentResponseDTO> getAllCourseLessonContents() {
        User currentMentor = getCurrentUser();
        
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can access course lesson content");
        }

        List<String> courseIds = courseRepository.findByCreator(currentMentor)
            .stream()
            .map(course -> course.getId())
            .collect(Collectors.toList());

        List<CourseLessonContent> contents = courseLessonContentRepository
            .findAll()
            .stream()
            .filter(content -> courseIds.contains(content.getCourseLesson().getCourse().getId()))
            .collect(Collectors.toList());
        
        return contents.stream()
            .map(courseLessonContentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public CourseLessonContentResponseDTO getCourseLessonContentById(String id) {
        User currentMentor = getCurrentUser();
        
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can access course lesson content");
        }

        CourseLessonContent content = courseLessonContentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course lesson content not found"));

        boolean isMentorCourse = courseRepository.findById(content.getCourseLesson().getCourse().getId())
            .map(course -> course.getCreator().getId().equals(currentMentor.getId()))
            .orElse(false);

        if (!isMentorCourse) {
            throw new SecurityException("You don't have permission to view this course lesson content");
        }

        return courseLessonContentMapper.toResponseDTO(content);
    }

    public CourseLessonContentResponseDTO createContent(CourseLessonContentCreateDTO dto) {
        User currentMentor = getCurrentUser();
        
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can create course lesson content");
        }

        CourseLessonContent content = new CourseLessonContent();
        content.setPosition(dto.getPosition());
        content.setType(dto.getType());
        content.setValue(dto.getValue());
        CourseLesson existingCourseLesson = courseLessonRepository.findById(dto.getCourseLessonId()).orElseThrow();
        content.setCourseLesson(existingCourseLesson);

        CourseLessonContent savedContent = courseLessonContentRepository.save(content);
        return courseLessonContentMapper.toResponseDTO(savedContent);
    }

    public CourseLessonContentResponseDTO updateContent(String id, CourseLessonContentCreateDTO dto) {
        User currentMentor = getCurrentUser();
        
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can update course lesson content");
        }

        CourseLessonContent content = courseLessonContentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course lesson content not found"));

        /*boolean isMentorCourse = courseRepository.findById(content.getCourseLesson().getCourse().getId())
            .map(course -> course.getCreator().getId().equals(currentMentor.getId()))
            .orElse(false);

        if (!isMentorCourse) {
            throw new SecurityException("You don't have permission to update this course lesson content");
        } */

        content.setPosition(dto.getPosition());
        content.setType(dto.getType());
        content.setValue(dto.getValue());
        CourseLesson existingCourseLesson = courseLessonRepository.findById(dto.getCourseLessonId()).orElseThrow();
        content.setCourseLesson(existingCourseLesson);


        CourseLessonContent updatedContent = courseLessonContentRepository.save(content);
        return courseLessonContentMapper.toResponseDTO(updatedContent);
    }

    public void deleteContent(String id) {
        User currentMentor = getCurrentUser();
        
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can delete course lesson content");
        }

        CourseLessonContent content = courseLessonContentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course lesson content not found"));

        // Get the associated CourseLesson
        String courseLessonId = content.getCourseLesson().getId();

        // Delete the content
        courseLessonContentRepository.delete(content);

        // Reassign positions for remaining content in the same CourseLesson
        List<CourseLessonContent> remainingContents = courseLessonContentRepository
            .findByCourseLessonId(courseLessonId)
            .stream()
            .sorted((a, b) -> Integer.compare(a.getPosition(), b.getPosition()))
            .collect(Collectors.toList());

        // Update positions sequentially starting from 1
        for (int i = 0; i < remainingContents.size(); i++) {
            remainingContents.get(i).setPosition(i + 1);
        }

        // Save the updated content items
        courseLessonContentRepository.saveAll(remainingContents);
    }
}