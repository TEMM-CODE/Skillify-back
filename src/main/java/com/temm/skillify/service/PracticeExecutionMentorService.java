package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.PracticeExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.PracticeExecutionMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.PracticeExecutionRepository;
import com.temm.skillify.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeExecutionMentorService {

    private final PracticeExecutionRepository practiceExecutionRepository;
    private final ClassroomRepository classroomRepository;
    private final PracticeRepository practiceRepository;
    private final PracticeExecutionMapper practiceExecutionMapper;
    private final UserService userService;

    // Get current authenticated mentor
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // Validate if current user is a mentor
    private void validateMentorRole(User user) {
        if (!UserRole.MENTOR.equals(user.getRole())) {
            throw new SecurityException("Only mentors can perform this action");
        }
    }

    // Validate if the current user is the mentor of the classroom related to the practice execution
    private void validateMentorOwnership(User currentMentor, PracticeExecution execution) {
        Practice practice = execution.getPractice();
        if (practice != null && practice.getClassroom() != null) {
            if (!Objects.equals(currentMentor.getId(), practice.getClassroom().getMentor().getId())) {
                throw new SecurityException("You can only access executions from your own classrooms");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PracticeExecutionResponseDTO> getAllMentorPracticeExecutions() {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        // Get all classrooms where the current user is the mentor
        List<Classroom> mentorClassrooms = classroomRepository.findByMentor(currentMentor);
        
        // Get all practices related to these classrooms
        List<Practice> practices = practiceRepository.findByClassroomIn(mentorClassrooms);

        // Get all practice executions related to these practices
        List<PracticeExecution> executions = practiceExecutionRepository.findByPracticeIn(practices);
        
        return practiceExecutionMapper.toResponseDTOList(executions);
    }

    @Transactional(readOnly = true)
    public PracticeExecutionResponseDTO getPracticeExecutionById(String id) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        PracticeExecution execution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateMentorOwnership(currentMentor, execution);
        return practiceExecutionMapper.toResponseDTO(execution);
    }

    @Transactional
    public PracticeExecutionResponseDTO createPracticeExecution(PracticeExecutionCreateDTO createDTO) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        // Convert DTO to entity (includes duration from createDTO)
        PracticeExecution execution = practiceExecutionMapper.toEntity(createDTO);
        
        // Set the student from userService since studentId is provided in DTO
        if (createDTO.getStudentId() != null) {
            User student = userService.findById(createDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + createDTO.getStudentId()));
            execution.setStudent(student);
        }

        // Validate and set the practice
        if (createDTO.getPracticeId() != null) {
            Practice practice = practiceRepository.findById(createDTO.getPracticeId())
                .orElseThrow(() -> new RuntimeException("Practice not found with id: " + createDTO.getPracticeId()));
            execution.setPractice(practice);
            
            // Validate that the practice belongs to one of the mentor's classrooms
            if (practice.getClassroom() != null) {
                Classroom classroom = classroomRepository.findById(practice.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
                if (!Objects.equals(currentMentor.getId(), classroom.getMentor().getId())) {
                    throw new SecurityException("You can only create executions for practices in your own classrooms");
                }
            }
        }

        PracticeExecution savedExecution = practiceExecutionRepository.save(execution);
        return practiceExecutionMapper.toResponseDTO(savedExecution);
    }

    @Transactional
    public PracticeExecutionResponseDTO updatePracticeExecution(String id, PracticeExecutionCreateDTO updateDTO) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        PracticeExecution existingExecution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateMentorOwnership(currentMentor, existingExecution);

        // Update entity from DTO (includes duration from updateDTO)
        practiceExecutionMapper.updateEntityFromDTO(existingExecution, updateDTO);
        
        // Update student if provided
        if (updateDTO.getStudentId() != null) {
            User student = userService.findById(updateDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + updateDTO.getStudentId()));
            existingExecution.setStudent(student);
        }

        // Update practice if provided and validate ownership
        if (updateDTO.getPracticeId() != null) {
            Practice practice = practiceRepository.findById(updateDTO.getPracticeId())
                .orElseThrow(() -> new RuntimeException("Practice not found with id: " + updateDTO.getPracticeId()));
            existingExecution.setPractice(practice);
            
            if (practice.getClassroom() != null) {
                Classroom classroom = classroomRepository.findById(practice.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
                if (!Objects.equals(currentMentor.getId(), classroom.getMentor().getId())) {
                    throw new SecurityException("You can only update executions for practices in your own classrooms");
                }
            }
        }

        PracticeExecution updatedExecution = practiceExecutionRepository.save(existingExecution);
        return practiceExecutionMapper.toResponseDTO(updatedExecution);
    }

    @Transactional
    public void deletePracticeExecution(String id) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        PracticeExecution execution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateMentorOwnership(currentMentor, execution);
        practiceExecutionRepository.delete(execution);
    }
}