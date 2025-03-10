package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        }
        
        return dto;
    }
}