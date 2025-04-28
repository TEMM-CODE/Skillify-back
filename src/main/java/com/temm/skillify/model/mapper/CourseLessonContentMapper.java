package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.model.entity.CourseLessonContent;
import org.springframework.stereotype.Component;

@Component
public class CourseLessonContentMapper {
    

    public CourseLessonContentResponseDTO toResponseDTO(CourseLessonContent entity) {
        if (entity == null) {
            return null;
        }

        CourseLessonContentResponseDTO dto = new CourseLessonContentResponseDTO();
        
        // Set base fields
        dto.setId(entity.getId());
        dto.setPosition(entity.getPosition());
        dto.setType(entity.getType());
        dto.setValue(entity.getValue());

  

        return dto;
    }
}