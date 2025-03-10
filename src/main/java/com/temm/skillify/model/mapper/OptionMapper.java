package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.model.entity.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionMapper {
    
    public OptionResponseDTO toResponseDTO(Option entity) {
        if (entity == null) {
            return null;
        }
        
        OptionResponseDTO dto = new OptionResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCorrect(entity.getCorrect());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
}