package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.EssayMapper;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EssayStudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private EssayMapper essayMapper;

    public List<EssayResponseDTO> getAvailableEssaysForStudent(Authentication authentication) {
        // Get the authenticated student
        User student = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get all essays from classrooms where the student is enrolled
        List<Essay> availableEssays = essayRepository.findAll().stream()
            .filter(essay -> {
                Classroom classroom = essay.getClassroom();
                return classroom != null && 
                       classroom.getStudents() != null && 
                       classroom.getStudents().contains(student);
            })
            .collect(Collectors.toList());

        // Convert to DTOs and return
        return availableEssays.stream()
            .map(essayMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    public EssayResponseDTO getEssayById(String id) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        
        // Find the essay or throw exception if not found
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Essay not found with id: " + id));
        
        // Get the classroom associated with the essay
        Classroom classroom = essay.getClassroom();
        if (classroom == null) {
            throw new IllegalStateException("This essay is not assigned to any classroom");
        }
        
        // Check if current user is a student in this classroom
        boolean isStudentInClassroom = classroom.getStudents().stream()
                .anyMatch(student -> student.getId().equals(currentUser.getId()));
        
        if (!isStudentInClassroom) {
            throw new SecurityException("You don't have permission to access this essay");
        }
        
        // Return the mapped DTO
        return essayMapper.toResponseDTO(essay);
    }

        private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}

