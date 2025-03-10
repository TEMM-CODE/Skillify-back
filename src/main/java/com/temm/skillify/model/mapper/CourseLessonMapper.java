package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.CourseLesson;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CourseLessonMapper {

    public CourseLessonResponseDTO toResponseDTO(CourseLesson entity) {
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
            
            // You might want to set the course here too, but be careful of infinite recursion
            // A simplified course representation could be used
            if (entity.getCourseLessonCategory().getCourse() != null) {
                CourseResponseDTO simpleCourseDTO = new CourseResponseDTO();
                simpleCourseDTO.setId(entity.getCourseLessonCategory().getCourse().getId());
                simpleCourseDTO.setName(entity.getCourseLessonCategory().getCourse().getName());
                categoryDTO.setCourse(simpleCourseDTO);
            }
            
            dto.setCourseLessonCategory(categoryDTO);
        }
        
        // Set classroom if available
        if (entity.getClassroom() != null) {
            ClassroomResponseDTO classroomDTO = new ClassroomResponseDTO();
            classroomDTO.setId(entity.getClassroom().getId());
            classroomDTO.setName(entity.getClassroom().getName());
            
            // Set mentor if available
            if (entity.getClassroom().getMentor() != null) {
                UserResponseDTO mentorDTO = new UserResponseDTO();
                mentorDTO.setId(entity.getClassroom().getMentor().getId());
                mentorDTO.setName(entity.getClassroom().getMentor().getName());
                mentorDTO.setEmail(entity.getClassroom().getMentor().getEmail());
                mentorDTO.setRole(entity.getClassroom().getMentor().getRole());
                
                classroomDTO.setMentor(mentorDTO);
            }
            
            // Set students if available
            if (entity.getClassroom().getStudents() != null) {
                classroomDTO.setStudents(entity.getClassroom().getStudents().stream()
                        .map(student -> {
                            UserResponseDTO studentDTO = new UserResponseDTO();
                            studentDTO.setId(student.getId());
                            studentDTO.setName(student.getName());
                            studentDTO.setEmail(student.getEmail());
                            studentDTO.setRole(student.getRole());
                            return studentDTO;
                        })
                        .collect(Collectors.toSet()));
            }
            
            dto.setClassroom(classroomDTO);
        }
        
        return dto;
    }
}