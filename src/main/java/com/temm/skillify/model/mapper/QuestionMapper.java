package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.QuestionSuperAdminType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final UserMapper userMapper;
    private final OptionMapper optionMapper;
    private final CourseMapper courseMapper;
    private final QuestionContentMapper questionContentMapper; // Added dependency

    public QuestionResponseDTO toResponseDTO(Question entity) {
        if (entity == null) {
            return null;
        }
        
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getMentor() != null) {
            dto.setMentor(userMapper.toResponseDTO(entity.getMentor()));
        }
        
        if (entity.getCourse() != null) {
            dto.setCourse(courseMapper.toResponseDTO(entity.getCourse()));
        }
        
        if (entity.getOptions() != null && !entity.getOptions().isEmpty()) {
            dto.setOptions(entity.getOptions().stream()
                    .map(optionMapper::toResponseDTO)
                    .collect(Collectors.toSet()));
        } else {
            dto.setOptions(new HashSet<>());
        }
        
        if (entity.getSuperAdminTypes() != null && !entity.getSuperAdminTypes().isEmpty()) {
            dto.setSuperAdminTypes(entity.getSuperAdminTypes());
        }
        
        if (entity.getContent() != null && !entity.getContent().isEmpty()) {
            dto.setContent(entity.getContent().stream()
                    .map(questionContentMapper::toResponseDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setContent(new ArrayList<>());
        }

        return dto;
    }
    
    public Question toEntity(QuestionCreateDTO dto, User mentor) {
        if (dto == null) {
            return null;
        }
        
        Question entity = new Question();
        entity.setTitle(dto.getTitle());
        entity.setMentor(mentor);
        entity.setOptions(new HashSet<>());
        
        if (dto.getSuperAdminTypes() != null && !dto.getSuperAdminTypes().isEmpty()) {
            entity.setSuperAdminTypes(dto.getSuperAdminTypes());
        }
        
        return entity;
    }
}