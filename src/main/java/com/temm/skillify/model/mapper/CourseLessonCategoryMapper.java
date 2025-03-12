package com.temm.skillify.model.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;

@Component
@RequiredArgsConstructor
public class CourseLessonCategoryMapper {
    private final CourseMapper courseMapper;
    
    public CourseLessonCategoryResponseDTO toResponseDTO(CourseLessonCategory entity) {
        if (entity == null) {
            return null;
        }
        
        CourseLessonCategoryResponseDTO dto = new CourseLessonCategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        
        // Map course to CourseResponseDTO
        if (entity.getCourse() != null) {
            CourseResponseDTO courseDTO = courseMapper.toResponseDTO(entity.getCourse());
            dto.setCourse(courseDTO);
        }
        
        // Map other BaseResponseDTO fields if they exist
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() != null) {
            dto.setUpdatedAt(entity.getUpdatedAt());
        }
        
        return dto;
    }
}