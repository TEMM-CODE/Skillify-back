package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.ClassroomCreateDTO;
import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.MentorMembership;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.ClassroomMapper;
import com.temm.skillify.repository.ClassroomAccessTokenRepository;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.repository.MentorMembershipRepository;
import com.temm.skillify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomAdminService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomAccessTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ClassroomMapper classroomMapper;
    private final CourseRepository courseRepository;
    private final MentorMembershipRepository mentorMembershipRepository;

  
    public List<ClassroomResponseDTO> findAllClassrooms() {
        User currentAdmin = getCurrentUser();
        List<User> mentors = mentorMembershipRepository.findByAdmin(currentAdmin)
                .stream()
                .map(MentorMembership::getMentor)
                .collect(Collectors.toList());

        return mentors.stream()
                .flatMap(mentor -> classroomRepository.findByMentor(mentor).stream())
                .map(classroomMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public Optional<ClassroomResponseDTO> findClassroomById(String id) {
        return classroomRepository.findById(id)
                .map(classroomMapper::toResponseDTO);
    }

    public ClassroomResponseDTO createClassroom(ClassroomCreateDTO classroomDTO) {
        Classroom classroom = new Classroom();
        classroom.setName(classroomDTO.getName());
        
        // Set mentor if provided
        if (classroomDTO.getMentorId() != null && !classroomDTO.getMentorId().isEmpty()) {
            User mentor = userRepository.findById(classroomDTO.getMentorId())
                    .orElseThrow(() -> new EntityNotFoundException("Mentor not found with id: " + classroomDTO.getMentorId()));
            classroom.setMentor(mentor);
        }
        
        // Set students if provided
        if (classroomDTO.getStudentIds() != null && !classroomDTO.getStudentIds().isEmpty()) {
            Set<User> students = classroomDTO.getStudentIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id)))
                    .collect(Collectors.toSet());
            classroom.setStudents(students);
        } else {
            classroom.setStudents(new HashSet<>());
        }

        // Set courses if provided
        if (classroomDTO.getCourseIds() != null && !classroomDTO.getCourseIds().isEmpty()) {
            Set<Course> courses = classroomDTO.getCourseIds().stream()
                    .map(id -> courseRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id)))
                    .collect(Collectors.toSet());
            classroom.setCourses(courses);
        } else {
            classroom.setCourses(new HashSet<>());
        }
        
        
        Classroom savedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(savedClassroom);
    }

    public ClassroomResponseDTO editCourseClassrooms(String classroomId, ClassroomCreateDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));

        // Update courses if provided
        if (classroomDTO.getCourseIds() != null) {
            Set<Course> courses = classroomDTO.getCourseIds().stream()
                    .map(id -> courseRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id)))
                    .collect(Collectors.toSet());
            classroom.setCourses(courses);
        } else {
            classroom.setCourses(new HashSet<>());
        }

        Classroom updatedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }

    public ClassroomResponseDTO updateClassroom(String id, ClassroomCreateDTO classroomDTO) {
        Classroom existingClassroom = classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + id));
        
        // Update name if provided
        existingClassroom.setName(classroomDTO.getName());
        
        // Update mentor if provided
        if (classroomDTO.getMentorId() != null) {
            User mentor = userRepository.findById(classroomDTO.getMentorId())
                    .orElseThrow(() -> new EntityNotFoundException("Mentor not found with id: " + classroomDTO.getMentorId()));
            existingClassroom.setMentor(mentor);
        }
        
        // Update students if provided
        if (classroomDTO.getStudentIds() != null) {
            Set<User> students = classroomDTO.getStudentIds().stream()
                    .map(studentId -> userRepository.findById(studentId)
                            .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId)))
                    .collect(Collectors.toSet());
            existingClassroom.setStudents(students);
        }
        
        Classroom updatedClassroom = classroomRepository.save(existingClassroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }

    public void deleteClassroom(String id) {
        classroomRepository.deleteById(id);
    }

    public List<ClassroomAccessToken> findTokensByClassroomId(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        return tokenRepository.findByClassroom(classroom);
    }

    public ClassroomAccessToken createAccessToken(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        ClassroomAccessToken token = new ClassroomAccessToken();
        token.setClassroom(classroom);
        token.setToken(UUID.randomUUID().toString());
        return tokenRepository.save(token);
    }

    public void deleteAccessToken(String tokenId) {
        tokenRepository.deleteById(tokenId);
    }

    public ClassroomResponseDTO updateClassroomMentor(String classroomId, String mentorId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor not found with id: " + mentorId));
        
        classroom.setMentor(mentor);
        Classroom updatedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }

    public ClassroomResponseDTO updateClassroomStudents(String classroomId, Set<String> studentIds) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        
        Set<User> students = studentIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id)))
                .collect(Collectors.toSet());
        
        classroom.setStudents(students);
        Classroom updatedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }
}