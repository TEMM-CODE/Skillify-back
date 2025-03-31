package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.dto.response.ClassroomWithCoursesResponseDTO;
import com.temm.skillify.model.dto.response.ClassroomCourseReturnDTO;
import com.temm.skillify.model.entity.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClassroomMapper {

    @Autowired
    private UserMapper userMapper;

    public ClassroomResponseDTO toResponseDTO(Classroom classroom) {
        if (classroom == null) {
            return null;
        }
        
        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setUpdatedAt(classroom.getUpdatedAt());
        
        if (classroom.getMentor() != null) {
            dto.setMentor(userMapper.toResponseDTO(classroom.getMentor()));
        }
        
        if (classroom.getStudents() != null) {
            dto.setStudents(classroom.getStudents().stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }

    public ClassroomWithCoursesResponseDTO toWithCoursesResponseDTO(Classroom classroom) {
        if (classroom == null) {
            return null;
        }

        ClassroomWithCoursesResponseDTO dto = new ClassroomWithCoursesResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setUpdatedAt(classroom.getUpdatedAt());

        if (classroom.getMentor() != null) {
            dto.setMentor(userMapper.toResponseDTO(classroom.getMentor()));
        }

        if (classroom.getStudents() != null) {
            dto.setStudents(classroom.getStudents().stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toSet()));
        }

        if (classroom.getCourses() != null) {
            dto.setCourses(classroom.getCourses().stream()
                    .map(course -> {
                        ClassroomCourseReturnDTO courseDTO = new ClassroomCourseReturnDTO();
                        courseDTO.setId(course.getId());
                        courseDTO.setName(course.getName());
                        return courseDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}