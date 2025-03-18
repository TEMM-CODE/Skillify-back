package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.GoalRequestDTO;
import com.temm.skillify.model.dto.response.GoalResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoalMapper {

    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private ClassroomMapper classroomMapper;

    public Goal toEntity(GoalRequestDTO dto) {
        Goal goal = new Goal();
        
        // Map classroom IDs to Classroom entities
        List<Classroom> classrooms = dto.getClassroomIds().stream()
                .map(classroomRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
                
        goal.setClassrooms(classrooms);
        goal.setNumber(dto.getNumber());
        goal.setType(dto.getType());
        goal.setOpeningDate(dto.getOpeningDate());
        goal.setFinalDate(dto.getFinalDate());
        
        return goal;
    }

    public GoalResponseDTO toResponseDTO(Goal entity) {
        if (entity == null) {
            return null;
        }
        
        GoalResponseDTO dto = new GoalResponseDTO();
        
        dto.setId(entity.getId());
        dto.setClassrooms(entity.getClassrooms().stream()
                .map(classroomMapper::toResponseDTO)
                .collect(Collectors.toList()));
        dto.setNumber(entity.getNumber());
        dto.setType(entity.getType());
        dto.setOpeningDate(entity.getOpeningDate());
        dto.setFinalDate(entity.getFinalDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }

    public List<GoalResponseDTO> toResponseDTOList(List<Goal> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDTO(GoalRequestDTO dto, Goal entity) {
        // Map classroom IDs to Classroom entities
        List<Classroom> classrooms = dto.getClassroomIds().stream()
                .map(classroomRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
                
        entity.setClassrooms(classrooms);
        entity.setNumber(dto.getNumber());
        entity.setType(dto.getType());
        entity.setOpeningDate(dto.getOpeningDate());
        entity.setFinalDate(dto.getFinalDate());
    }
}