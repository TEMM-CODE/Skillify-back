package com.temm.skillify.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.service.EssayService;
import com.temm.skillify.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EssayExecutionMapper {

    private final EssayRepository essayService;
    private final UserService userService;
    private final EssayMapper essayMapper;
    private final UserMapper userMapper;
    
    public EssayExecutionResponseDTO toResponseDTO(EssayExecution essayExecution) {
        if (essayExecution == null) {
            return null;
        }
        
        EssayExecutionResponseDTO dto = new EssayExecutionResponseDTO();
        dto.setId(essayExecution.getId());
        dto.setCreatedAt(essayExecution.getCreatedAt());
        dto.setUpdatedAt(essayExecution.getUpdatedAt());
        
        dto.setEssay(essayMapper.toResponseDTO(essayExecution.getEssay()));
        dto.setStudent(userMapper.toResponseDTO(essayExecution.getStudent()));
        dto.setText(essayExecution.getText());
        
        return dto;
    }
    
    public List<EssayExecutionResponseDTO> toResponseDTOList(List<EssayExecution> executions) {
        return executions.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public EssayExecution toEntity(EssayExecutionCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        
        EssayExecution essayExecution = new EssayExecution();
        
        // Fetch related entities
        Essay essay = essayService.findById(createDTO.getEssayId())
                .orElseThrow(() -> new RuntimeException("Essay not found"));
        User student = userService.findById(createDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        essayExecution.setEssay(essay);
        essayExecution.setStudent(student);
        essayExecution.setText(createDTO.getText());
        
        return essayExecution;
    }
    
    public void updateEntityFromDTO(EssayExecution essayExecution, EssayExecutionCreateDTO updateDTO) {
        if (updateDTO == null) {
            return;
        }
        
        if (updateDTO.getEssayId() != null) {
            Essay essay = essayService.findById(updateDTO.getEssayId())
                    .orElseThrow(() -> new RuntimeException("Essay not found"));
            essayExecution.setEssay(essay);
        }
        
        if (updateDTO.getStudentId() != null) {
            User student = userService.findById(updateDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            essayExecution.setStudent(student);
        }
        
        if (updateDTO.getText() != null) {
            essayExecution.setText(updateDTO.getText());
        }
    }
}