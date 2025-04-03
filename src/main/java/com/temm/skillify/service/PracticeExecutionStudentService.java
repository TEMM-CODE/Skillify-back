package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.PracticeExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.PracticeExecutionMapper;
import com.temm.skillify.repository.PracticeExecutionRepository;
import com.temm.skillify.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PracticeExecutionStudentService {

    private final PracticeExecutionRepository practiceExecutionRepository;
    private final PracticeRepository practiceRepository;  // Added new dependency
    private final PracticeExecutionMapper practiceExecutionMapper;
    private final UserService userService;

    // Get current authenticated student
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // Validate if current user is a student
    private void validateStudentRole(User user) {
        if (!UserRole.ESTUDANTE.equals(user.getRole())) {
            throw new SecurityException("Only students can perform this action");
        }
    }

    // Validate if the current user is the student who authored the practice execution
    private void validateStudentOwnership(User currentStudent, PracticeExecution execution) {
        if (!Objects.equals(currentStudent.getId(), execution.getStudent().getId())) {
            throw new SecurityException("You can only access your own practice executions");
        }
    }

    @Transactional(readOnly = true)
    public List<PracticeExecutionResponseDTO> getAllStudentPracticeExecutions() {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        List<PracticeExecution> executions = practiceExecutionRepository.findByStudentId(currentStudent.getId());
        return practiceExecutionMapper.toResponseDTOList(executions);
    }

    @Transactional(readOnly = true)
    public PracticeExecutionResponseDTO getPracticeExecutionById(String id) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        PracticeExecution execution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateStudentOwnership(currentStudent, execution);
        return practiceExecutionMapper.toResponseDTO(execution);
    }

    @Transactional
    public PracticeExecutionResponseDTO createPracticeExecution(PracticeExecutionCreateDTO createDTO) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        // Ensure the studentId in DTO matches current user
        if (!Objects.equals(currentStudent.getId(), createDTO.getStudentId())) {
            throw new SecurityException("You can only create practice executions for yourself");
        }

        // Get the Practice entity
        Practice practice = practiceRepository.findById(createDTO.getPracticeId())
            .orElseThrow(() -> new RuntimeException("Practice not found with id: " + createDTO.getPracticeId()));

        // Count existing executions for this student and practice
        long existingExecutionsCount = practiceExecutionRepository
            .countByStudentIdAndPracticeId(currentStudent.getId(), createDTO.getPracticeId());

        // Check if numberOfAllowedAttempts is null (unlimited) or if we're within the limit
        Integer allowedAttempts = practice.getNumberOfAllowedAttempts();
        if (allowedAttempts != null && existingExecutionsCount >= allowedAttempts) {
            throw new IllegalStateException(
                "Maximum number of attempts (" + allowedAttempts + ") reached for this practice");
        }

        PracticeExecution execution = practiceExecutionMapper.toEntity(createDTO);
        PracticeExecution savedExecution = practiceExecutionRepository.save(execution);
        return practiceExecutionMapper.toResponseDTO(savedExecution);
    }

    @Transactional
    public PracticeExecutionResponseDTO updatePracticeExecution(String id, PracticeExecutionCreateDTO updateDTO) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        PracticeExecution existingExecution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateStudentOwnership(currentStudent, existingExecution);

        // Ensure the studentId in DTO matches current user
        if (!Objects.equals(currentStudent.getId(), updateDTO.getStudentId())) {
            throw new SecurityException("You can only update practice executions for yourself");
        }

        practiceExecutionMapper.updateEntityFromDTO(existingExecution, updateDTO);
        PracticeExecution updatedExecution = practiceExecutionRepository.save(existingExecution);
        return practiceExecutionMapper.toResponseDTO(updatedExecution);
    }
}