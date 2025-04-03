package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.PracticeCreateDTO;
import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.Practice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PracticeMapper {
    
    private final UserMapper userMapper;
    private final ClassroomMapper classroomMapper;
    private final QuestionMapper questionMapper;
    private final CourseMapper courseMapper;  // Added CourseMapper
    
    public Practice toEntity(PracticeCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Practice practice = new Practice();
        practice.setTitle(dto.getTitle());
        practice.setNumberOfQuestions(dto.getNumberOfQuestions());
        practice.setDuracao(dto.getDuracao());
        practice.setOpeningDate(dto.getOpeningDate());
        practice.setMaximumDate(dto.getMaximumDate());
        practice.setNumberOfAllowedAttempts(dto.getNumberOfAllowedAttempts());
        
        if (dto.getClassroomId() != null) {
            Classroom classroom = new Classroom();
            classroom.setId(dto.getClassroomId());
            practice.setClassroom(classroom);
        }
        
        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = dto.getCourseIds().stream()
                .map(courseId -> {
                    Course course = new Course();
                    course.setId(courseId);
                    return course;
                })
                .collect(Collectors.toList());
            practice.setCourses(courses);
        } else {
            practice.setCourses(new ArrayList<>());
        }
        
        practice.setQuestions(new HashSet<>());
        
        return practice;
    }
    
    public PracticeResponseDTO toResponseDTO(Practice entity) {
        if (entity == null) {
            return null;
        }
        
        PracticeResponseDTO dto = new PracticeResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setNumberOfQuestions(entity.getNumberOfQuestions());
        dto.setDuracao(entity.getDuracao());
        dto.setOpeningDate(entity.getOpeningDate());
        dto.setMaximumDate(entity.getMaximumDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setNumberOfAllowedAttempts(entity.getNumberOfAllowedAttempts());
        
        if (entity.getMentor() != null) {
            dto.setMentor(userMapper.toResponseDTO(entity.getMentor()));
        }
        
        if (entity.getClassroom() != null) {
            dto.setClassroom(classroomMapper.toResponseDTO(entity.getClassroom()));
        }
        
        if (entity.getCourses() != null && !entity.getCourses().isEmpty()) {
            dto.setCourses(entity.getCourses().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList()));
        }
        
        if (entity.getQuestions() != null && !entity.getQuestions().isEmpty()) {
            dto.setQuestions(entity.getQuestions().stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}