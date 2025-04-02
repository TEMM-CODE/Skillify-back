package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.EssayExecutionMapper;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.EssayRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayExecutionService {
    private final EssayExecutionRepository essayExecutionRepository;
    private final EssayExecutionMapper essayExecutionMapper;
    private final UserService userService;
    private final EssayRepository essayRepository;

    public List<EssayExecutionResponseDTO> findAllDTOs() {
        List<EssayExecution> executions = essayExecutionRepository.findAll();
        return essayExecutionMapper.toResponseDTOList(executions);
    }

    public List<EssayExecutionResponseDTO> findAllDTOsByStudentEmail(String email) {
        User student = userService.findByEmail(email).orElseThrow(() -> 
            new RuntimeException("Student not found with email: " + email));
        List<EssayExecution> executions = essayExecutionRepository.findByStudent(student);
        return essayExecutionMapper.toResponseDTOList(executions);
    }

    public Optional<EssayExecutionResponseDTO> findDTOById(String id) {
        return essayExecutionRepository.findById(id)
                .map(essayExecutionMapper::toResponseDTO);
    }

    public Optional<EssayExecutionResponseDTO> findDTOByIdAndStudentEmail(String id, String email) {
        User student = userService.findByEmail(email).orElseThrow(() -> 
            new RuntimeException("Student not found with email: " + email));
        return essayExecutionRepository.findByIdAndStudent(id, student)
                .map(essayExecutionMapper::toResponseDTO);
    }

    public EssayExecutionResponseDTO save(EssayExecutionCreateDTO createDTO) {
        EssayExecution essayExecution = essayExecutionMapper.toEntity(createDTO);
        EssayExecution savedExecution = essayExecutionRepository.save(essayExecution);
        return essayExecutionMapper.toResponseDTO(savedExecution);
    }

    public EssayExecutionResponseDTO saveForStudent(EssayExecutionCreateDTO createDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = (User) authentication.getPrincipal();

        // Create entity from DTO
        EssayExecution essayExecution = new EssayExecution();
        essayExecution.setStudent(student);
        essayExecution.setText(createDTO.getText());
        
        // Set essay
        if (createDTO.getEssayId() != null) {
            essayExecution.setEssay(essayRepository.findById(createDTO.getEssayId())
                .orElseThrow(() -> new RuntimeException("Essay not found")));
        }
        
        EssayExecution savedExecution = essayExecutionRepository.save(essayExecution);
        return essayExecutionMapper.toResponseDTO(savedExecution);
    }
    
    public EssayExecutionResponseDTO update(String id, EssayExecutionCreateDTO updateDTO) {
        EssayExecution essayExecution = essayExecutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Essay execution not found with id: " + id));
        
        essayExecutionMapper.updateEntityFromDTO(essayExecution, updateDTO);
        EssayExecution updatedExecution = essayExecutionRepository.save(essayExecution);
        return essayExecutionMapper.toResponseDTO(updatedExecution);
    }

    public void deleteById(String id) {
        essayExecutionRepository.deleteById(id);
    }
}