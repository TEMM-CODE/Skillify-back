// EssayExecutionMentorService.java
package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.mapper.EssayExecutionMapper;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayExecutionMentorService {
    
    private final EssayExecutionRepository essayExecutionRepository;
    private final ClassroomRepository classroomRepository;
    private final EssayExecutionMapper essayExecutionMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public List<EssayExecutionResponseDTO> getAllEssayExecutions() {
        User currentMentor = getCurrentUser();
        
        // Verify mentor role
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can access essay executions");
        }

        // Get all classrooms where current user is mentor
        List<String> classroomIds = classroomRepository.findByMentor(currentMentor)
            .stream()
            .map(classroom -> classroom.getId())
            .collect(Collectors.toList());

        // Get all essay executions from mentor's classrooms
        List<EssayExecution> executions = essayExecutionRepository
            .findByEssayClassroomIdIn(classroomIds);
        
        return essayExecutionMapper.toResponseDTOList(executions);
    }

    public EssayExecutionResponseDTO getEssayExecutionById(String id) {
        User currentMentor = getCurrentUser();
        
        // Verify mentor role
        if (!currentMentor.getRole().equals(UserRole.MENTOR)) {
            throw new SecurityException("Only mentors can access essay executions");
        }

        EssayExecution execution = essayExecutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Essay execution not found"));

        // Verify if this execution belongs to one of mentor's classrooms
        boolean isMentorClassroom = classroomRepository.findById(execution.getEssay().getClassroom().getId())
            .map(classroom -> classroom.getMentor().getId().equals(currentMentor.getId()))
            .orElse(false);

        if (!isMentorClassroom) {
            throw new SecurityException("You don't have permission to view this essay execution");
        }

        return essayExecutionMapper.toResponseDTO(execution);
    }
}