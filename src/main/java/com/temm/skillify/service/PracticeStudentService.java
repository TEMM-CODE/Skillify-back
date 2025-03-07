package com.temm.skillify.service;



import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.PracticeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeStudentService {
    
    private final PracticeRepository practiceRepository;
    private final ClassroomRepository classroomRepository;
    private final UserService userService;
    private final MappingService mappingService;
    
    public List<PracticeResponseDTO> getAllPracticesForStudent(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // Get all classrooms where the student is enrolled
        Set<Classroom> studentClassrooms = student.getRole().name().equals("ESTUDANTE") ? 
                classroomRepository.findAll().stream()
                .filter(classroom -> classroom.getStudents().contains(student))
                .collect(Collectors.toSet()) : 
                Set.of();
        
        // Get all practices for the student's classrooms that are currently available
        LocalDateTime now = LocalDateTime.now();
        
        return studentClassrooms.stream()
                .flatMap(classroom -> practiceRepository.findByClassroom(classroom).stream())
                .filter(practice -> practice.getOpeningDate().isBefore(now) && 
                                   practice.getMaximumDate().isAfter(now))
                .map(mappingService::mapToPracticeResponseDTO)
                .collect(Collectors.toList());
    }
    
    public PracticeResponseDTO getPracticeById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        Practice practice = practiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Practice not found with id: " + id));
        
        // Verify the student is in the classroom for this practice
        Classroom classroom = practice.getClassroom();
        if (!classroom.getStudents().contains(student)) {
            throw new EntityNotFoundException("Practice not found or not accessible");
        }
        
        return mappingService.mapToPracticeResponseDTO(practice);
    }
    
    public List<PracticeResponseDTO> getPracticesByClassroom(String classroomId, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        
        // Verify the student is in this classroom
        if (!classroom.getStudents().contains(student)) {
            throw new EntityNotFoundException("Classroom not found or not accessible");
        }
        
        // Get available practices
        LocalDateTime now = LocalDateTime.now();
        
        return practiceRepository.findByClassroom(classroom).stream()
                .filter(practice -> practice.getOpeningDate().isBefore(now) && 
                                   practice.getMaximumDate().isAfter(now))
                .map(mappingService::mapToPracticeResponseDTO)
                .collect(Collectors.toList());
    }
}