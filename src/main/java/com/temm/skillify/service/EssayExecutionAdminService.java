package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayExecutionAdminService {
    
    private final EssayExecutionRepository essayExecutionRepository;
    private final EssayRepository essayRepository;
    private final UserRepository userRepository;
    
    public List<EssayExecution> findAll() {
        return essayExecutionRepository.findAll();
    }
    
    public Optional<EssayExecution> findById(String id) {
        return essayExecutionRepository.findById(id);
    }
    
    public List<EssayExecution> findByStudent(String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return essayExecutionRepository.findByStudent(student);
    }
    
    public EssayExecution create(EssayExecution execution, String studentId, String essayId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new EntityNotFoundException("Essay not found"));
        
        execution.setStudent(student);
        execution.setEssay(essay);
        
        return essayExecutionRepository.save(execution);
    }
    
    public EssayExecution update(String id, EssayExecution updatedExecution, String studentId, String essayId) {
        EssayExecution existingExecution = essayExecutionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Essay execution not found"));
        
        if (studentId != null) {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            existingExecution.setStudent(student);
        }
        
        if (essayId != null) {
            Essay essay = essayRepository.findById(essayId)
                    .orElseThrow(() -> new EntityNotFoundException("Essay not found"));
            existingExecution.setEssay(essay);
        }
        
        if (updatedExecution.getText() != null) {
            existingExecution.setText(updatedExecution.getText());
        }
        
        return essayExecutionRepository.save(existingExecution);
    }
    
    public void deleteById(String id) {
        essayExecutionRepository.deleteById(id);
    }
}