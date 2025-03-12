package com.temm.skillify.model.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.temm.skillify.model.dto.request.EssayCorrectionCreateDTO;
import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.service.EssayExecutionService;
import com.temm.skillify.service.EssayService;
import com.temm.skillify.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EssayCorrectionMapper {
    
    private final EssayRepository essayRepository;
    private final UserService userService;
    private final EssayExecutionRepository essayExecutionService;
    private final EssayMapper essayMapper;
    private final UserMapper userMapper;
    private final EssayExecutionMapper essayExecutionMapper;
    
    public EssayCorrectionResponseDTO toResponseDTO(EssayCorrection essayCorrection) {
        if (essayCorrection == null) {
            return null;
        }
        
        EssayCorrectionResponseDTO dto = new EssayCorrectionResponseDTO();
        dto.setId(essayCorrection.getId());
        dto.setCreatedAt(essayCorrection.getCreatedAt());
        dto.setUpdatedAt(essayCorrection.getUpdatedAt());
        
        dto.setEssay(essayMapper.toResponseDTO(essayCorrection.getEssay()));
        dto.setMentor(userMapper.toResponseDTO(essayCorrection.getMentor()));
        dto.setEssayExecution(essayExecutionMapper.toResponseDTO(essayCorrection.getEssayExecution()));
        
        dto.setEstruturaCoesaoComentario(essayCorrection.getEstruturaCoesaoComentario());
        dto.setArgumentacaoComentario(essayCorrection.getArgumentacaoComentario());
        dto.setConquistas(essayCorrection.getConquistas());
        dto.setCompetencia1Score(essayCorrection.getCompetencia1Score());
        dto.setCompetencia2Score(essayCorrection.getCompetencia2Score());
        dto.setCompetencia3Score(essayCorrection.getCompetencia3Score());
        dto.setCompetencia4Score(essayCorrection.getCompetencia4Score());
        dto.setCompetencia5Score(essayCorrection.getCompetencia5Score());
        
        return dto;
    }
    
    public List<EssayCorrectionResponseDTO> toResponseDTOList(List<EssayCorrection> corrections) {
        return corrections.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public EssayCorrection toEntity(EssayCorrectionCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        
        EssayCorrection essayCorrection = new EssayCorrection();
        
        // Fetch related entities
        Essay essay = essayRepository.findById(createDTO.getEssayId())
                .orElseThrow(() -> new RuntimeException("Essay not found"));
        User mentor = userService.findById(createDTO.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        EssayExecution essayExecution = essayExecutionService.findById(createDTO.getEssayExecutionId())
                .orElseThrow(() -> new RuntimeException("Essay execution not found"));
        
        essayCorrection.setEssay(essay);
        essayCorrection.setMentor(mentor);
        essayCorrection.setEssayExecution(essayExecution);
        
        essayCorrection.setEstruturaCoesaoComentario(createDTO.getEstruturaCoesaoComentario());
        essayCorrection.setArgumentacaoComentario(createDTO.getArgumentacaoComentario());
        essayCorrection.setConquistas(createDTO.getConquistas());
        essayCorrection.setCompetencia1Score(createDTO.getCompetencia1Score());
        essayCorrection.setCompetencia2Score(createDTO.getCompetencia2Score());
        essayCorrection.setCompetencia3Score(createDTO.getCompetencia3Score());
        essayCorrection.setCompetencia4Score(createDTO.getCompetencia4Score());
        essayCorrection.setCompetencia5Score(createDTO.getCompetencia5Score());
        
        return essayCorrection;
    }
    
    public void updateEntityFromDTO(EssayCorrection essayCorrection, EssayCorrectionCreateDTO updateDTO) {
        if (updateDTO.getEssayId() != null) {
            Essay essay = essayRepository.findById(updateDTO.getEssayId())
                    .orElseThrow(() -> new RuntimeException("Essay not found"));
            essayCorrection.setEssay(essay);
        }
        
        if (updateDTO.getMentorId() != null) {
            User mentor = userService.findById(updateDTO.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Mentor not found"));
            essayCorrection.setMentor(mentor);
        }
        
        if (updateDTO.getEssayExecutionId() != null) {
            EssayExecution essayExecution = essayExecutionService.findById(updateDTO.getEssayExecutionId())
                    .orElseThrow(() -> new RuntimeException("Essay execution not found"));
            essayCorrection.setEssayExecution(essayExecution);
        }
        
        if (updateDTO.getEstruturaCoesaoComentario() != null) {
            essayCorrection.setEstruturaCoesaoComentario(updateDTO.getEstruturaCoesaoComentario());
        }
        
        if (updateDTO.getArgumentacaoComentario() != null) {
            essayCorrection.setArgumentacaoComentario(updateDTO.getArgumentacaoComentario());
        }
        
        if (updateDTO.getConquistas() != null) {
            essayCorrection.setConquistas(updateDTO.getConquistas());
        }
        
        if (updateDTO.getCompetencia1Score() != null) {
            essayCorrection.setCompetencia1Score(updateDTO.getCompetencia1Score());
        }
        
        if (updateDTO.getCompetencia2Score() != null) {
            essayCorrection.setCompetencia2Score(updateDTO.getCompetencia2Score());
        }
        
        if (updateDTO.getCompetencia3Score() != null) {
            essayCorrection.setCompetencia3Score(updateDTO.getCompetencia3Score());
        }
        
        if (updateDTO.getCompetencia4Score() != null) {
            essayCorrection.setCompetencia4Score(updateDTO.getCompetencia4Score());
        }
        
        if (updateDTO.getCompetencia5Score() != null) {
            essayCorrection.setCompetencia5Score(updateDTO.getCompetencia5Score());
        }
    }
}