package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayCorrectionRepository;
import com.temm.skillify.repository.EssayExecutionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayCorrectionStudentService {
    
    private final EssayCorrectionRepository essayCorrectionRepository;
    private final EssayExecutionRepository essayExecutionRepository;
    private final UserService userService;
    private final MappingService mappingService;
    
    public List<EssayCorrectionResponseDTO> getAllEssayCorrectionsForStudent(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // Get all essay executions by this student
        List<EssayExecution> studentExecutions = essayExecutionRepository.findByStudent(student);
        
        // Get all corrections for the student's executions
        return studentExecutions.stream()
                .map(execution -> essayCorrectionRepository.findByEssayExecution(execution))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(mappingService::mapToEssayCorrectionResponseDTO)
                .collect(Collectors.toList());
    }
    
    public EssayCorrectionResponseDTO getEssayCorrectionById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        EssayCorrection correction = essayCorrectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Essay correction not found with id: " + id));
        
        // Verify that this correction belongs to the student
        EssayExecution execution = correction.getEssayExecution();
        if (!execution.getStudent().getId().equals(student.getId())) {
            throw new EntityNotFoundException("Essay correction not found or not accessible");
        }
        
        return mappingService.mapToEssayCorrectionResponseDTO(correction);
    }
    
    public EssayCorrectionResponseDTO getEssayCorrectionByExecutionId(String executionId, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // Find the execution and verify it belongs to the student
        EssayExecution execution = essayExecutionRepository.findByIdAndStudent(executionId, student)
                .orElseThrow(() -> new EntityNotFoundException("Essay execution not found with id: " + executionId));
        
        // Get the correction for this execution
        EssayCorrection correction = essayCorrectionRepository.findByEssayExecution(execution)
                .orElseThrow(() -> new EntityNotFoundException("No correction found for execution with id: " + executionId));
        
        return mappingService.mapToEssayCorrectionResponseDTO(correction);
    }
}