package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final UserMapper userMapper;
    private final OptionMapper optionMapper;
    
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
        
        if (entity.getOptions() != null && !entity.getOptions().isEmpty()) {
            dto.setOptions(entity.getOptions().stream()
                    .map(optionMapper::toResponseDTO)
                    .collect(Collectors.toSet()));
        } else {
            dto.setOptions(new HashSet<>());
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
        
        return entity;
    }
}