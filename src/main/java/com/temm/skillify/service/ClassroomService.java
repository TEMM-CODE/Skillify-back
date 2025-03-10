package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.ClassroomCreateDTO;
import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserService userService;

    // Entity methods - kept for backward compatibility
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public List<Classroom> findAllByMentorEmail(String email) {
        User mentor = userService.findByEmail(email).orElseThrow();
        return classroomRepository.findByMentor(mentor);
    }

    public Optional<Classroom> findById(String id) {
        return classroomRepository.findById(id);
    }

    public Optional<Classroom> findByIdAndMentorEmail(String id, String email) {
        User mentor = userService.findByEmail(email).orElseThrow();
        return classroomRepository.findByIdAndMentor(id, mentor);
    }

    public Classroom save(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Classroom saveForMentor(Classroom classroom, String mentorEmail) {
        User mentor = userService.findByEmail(mentorEmail).orElseThrow();
        classroom.setMentor(mentor);
        return classroomRepository.save(classroom);
    }

    public void deleteById(String id) {
        classroomRepository.deleteById(id);
    }

    // DTO methods
    public List<ClassroomResponseDTO> findAllDTOs() {
        return findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClassroomResponseDTO> findAllDTOsByMentorEmail(String email) {
        return findAllByMentorEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClassroomResponseDTO> findDTOById(String id) {
        return findById(id).map(this::convertToDTO);
    }

    public Optional<ClassroomResponseDTO> findDTOByIdAndMentorEmail(String id, String email) {
        return findByIdAndMentorEmail(id, email).map(this::convertToDTO);
    }

    public ClassroomResponseDTO createForMentor(ClassroomCreateDTO classroomDTO, String mentorEmail) {
        User mentor = userService.findByEmail(mentorEmail).orElseThrow();
        Classroom classroom = convertToEntity(classroomDTO);
        classroom.setMentor(mentor);
        return convertToDTO(classroomRepository.save(classroom));
    }

    public ClassroomResponseDTO update(String id, ClassroomCreateDTO classroomDTO) {
        Classroom existingClassroom = findById(id).orElseThrow();
        
        // Update fields from DTO
        existingClassroom.setName(classroomDTO.getName());
        
        // Handle students - convert IDs to User entities
        if (classroomDTO.getStudentIds() != null) {
            Set<User> students = classroomDTO.getStudentIds().stream()
                .map(studentId -> userService.findById(studentId).orElseThrow())
                .collect(Collectors.toSet());
            existingClassroom.setStudents(students);
        }
        
        // Only change mentor if explicitly provided and user has permission
        if (classroomDTO.getMentorId() != null) {
            User mentor = userService.findById(classroomDTO.getMentorId()).orElseThrow();
            existingClassroom.setMentor(mentor);
        }
        
        return convertToDTO(classroomRepository.save(existingClassroom));
    }

    // Helper methods
    private ClassroomResponseDTO convertToDTO(Classroom classroom) {
        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        
        // Convert mentor to DTO
        if (classroom.getMentor() != null) {
            dto.setMentor(convertUserToDTO(classroom.getMentor()));
        }
        
        // Convert students to DTOs
        if (classroom.getStudents() != null) {
            dto.setStudents(classroom.getStudents().stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }

    private UserResponseDTO convertUserToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTel(user.getTel());
        dto.setBiography(user.getBiography());
        dto.setEmailNotifications(user.isEmailNotifications());
        dto.setPushNotifications(user.isPushNotifications());
        dto.setWeeklyReport(user.isWeeklyReport());
        dto.setStudyReminder(user.isStudyReminder());
        dto.setRole(user.getRole());
        return dto;
    }

    private Classroom convertToEntity(ClassroomCreateDTO dto) {
        Classroom classroom = new Classroom();
        classroom.setName(dto.getName());
        
        // Convert student IDs to User entities
        if (dto.getStudentIds() != null) {
            Set<User> students = dto.getStudentIds().stream()
                .map(studentId -> userService.findById(studentId).orElseThrow())
                .collect(Collectors.toSet());
            classroom.setStudents(students);
        } else {
            classroom.setStudents(new HashSet<>());
        }
        
        // Convert mentor ID to User entity if provided
        if (dto.getMentorId() != null) {
            User mentor = userService.findById(dto.getMentorId()).orElseThrow();
            classroom.setMentor(mentor);
        }
        
        return classroom;
    }
}