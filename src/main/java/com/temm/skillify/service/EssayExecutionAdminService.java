package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.model.mapper.EssayExecutionMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayExecutionAdminService {
    private final EssayExecutionRepository essayExecutionRepository;
    private final UserRepository userRepository;
    private final EssayExecutionMapper essayExecutionMapper;
    
    public List<EssayExecutionResponseDTO> findAll() {
        List<EssayExecution> executions = essayExecutionRepository.findAll();
        return essayExecutionMapper.toResponseDTOList(executions);
    }
    
    public Optional<EssayExecutionResponseDTO> findById(String id) {
        return essayExecutionRepository.findById(id)
            .map(essayExecutionMapper::toResponseDTO);
    }
    
    public List<EssayExecutionResponseDTO> findByStudent(String studentId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        List<EssayExecution> executions = essayExecutionRepository.findByStudent(student);
        return essayExecutionMapper.toResponseDTOList(executions);
    }
    
    public EssayExecutionResponseDTO create(EssayExecutionCreateDTO createDTO) {
        EssayExecution execution = essayExecutionMapper.toEntity(createDTO);
        EssayExecution saved = essayExecutionRepository.save(execution);
        return essayExecutionMapper.toResponseDTO(saved);
    }
    
    public EssayExecutionResponseDTO update(String id, EssayExecutionCreateDTO updateDTO) {
        EssayExecution existingExecution = essayExecutionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Essay execution not found"));
        
        essayExecutionMapper.updateEntityFromDTO(existingExecution, updateDTO);
        EssayExecution updated = essayExecutionRepository.save(existingExecution);
        return essayExecutionMapper.toResponseDTO(updated);
    }
    
    public void deleteById(String id) {
        essayExecutionRepository.deleteById(id);
    }
}