package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.request.EssayCreateDTO;
import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EssayMapper {

    @Autowired
    private ClassroomMapper classroomMapper;

    public EssayResponseDTO toResponseDTO(Essay essay) {
        if (essay == null) {
            return null;
        }
        
        EssayResponseDTO dto = new EssayResponseDTO();
        dto.setId(essay.getId());
        dto.setTheme(essay.getTheme());
        dto.setDescription(essay.getDescription());
        dto.setMinWords(essay.getMinWords());
        dto.setMaxDate(essay.getMaxDate());
        dto.setCreatedAt(essay.getCreatedAt());
        dto.setUpdatedAt(essay.getUpdatedAt());
        
        if (essay.getClassroom() != null) {
            dto.setClassroom(classroomMapper.toResponseDTO(essay.getClassroom()));
        }
        
        return dto;
    }

    public Essay toEntity(EssayCreateDTO dto, Classroom classroom) {
        if (dto == null) {
            return null;
        }
        
        Essay essay = new Essay();
        essay.setTheme(dto.getTheme());
        essay.setDescription(dto.getDescription());
        essay.setMinWords(dto.getMinWords());
        essay.setMaxDate(dto.getMaxDate());
        essay.setClassroom(classroom);
        
        return essay;
    }

    public void updateEntityFromDTO(Essay essay, EssayCreateDTO dto) {
        if (essay == null || dto == null) {
            return;
        }
        
        essay.setTheme(dto.getTheme());
        essay.setDescription(dto.getDescription());
        essay.setMinWords(dto.getMinWords());
        essay.setMaxDate(dto.getMaxDate());
        // We don't update the classroom as mentioned in your original service
    }
}