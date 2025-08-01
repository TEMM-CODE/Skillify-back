package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.model.entity.CourseLessonContent;
import com.temm.skillify.model.enums.CourseLessonContentType;
import com.temm.skillify.service.S3Service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseLessonContentMapper {

    private final S3Service s3Service;
    

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

  if (entity.getType() == CourseLessonContentType.VIDEO) {
            dto.setUrl(s3Service.generatePresignedUrl(entity.getValue()).toString());
        }

        return dto;
    }
}