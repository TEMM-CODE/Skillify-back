package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.TutorSessionMapper;
import com.temm.skillify.repository.TutorSessionRepository;
import com.temm.skillify.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorSessionService {

    private final TutorSessionRepository sessionRepository;
    private final ClassroomService classroomService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TutorSessionMapper tutorSessionMapper;

    public List<TutorSessionResponseDTO> findAll() {
        return sessionRepository.findAll().stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByMentor(User mentor) {
        return sessionRepository.findByMentor(mentor).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByStudent(User student) {
        return sessionRepository.findByStudent(student).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByMentorAndDate(String mentorId, LocalDate date) {
        User mentor = userRepository.findById(mentorId).orElseThrow();
        return sessionRepository.findByMentorAndDate(mentor, date).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<TutorSessionResponseDTO> findById(String id) {
        return sessionRepository.findById(id)
                .map(tutorSessionMapper::toResponseDTO);
    }

    public TutorSessionResponseDTO save(TutorSession session) {
        return tutorSessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    public void deleteById(String id) {
        sessionRepository.deleteById(id);
    }

    // Helper method to check if a user is a mentor in any of student's classrooms
    public boolean isMentorOfStudentClassroom(User student, User potentialMentor) {
        return classroomService.findAll().stream()
                .filter(classroom -> classroom.getStudents().contains(student))
                .anyMatch(classroom -> classroom.getMentor().equals(potentialMentor));
    }

    public List<TutorSessionResponseDTO> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findByMentor(mentor).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TutorSessionResponseDTO findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findById(id)
                .filter(session -> session.getMentor().getId().equals(mentor.getId()))
                .map(tutorSessionMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
    }

    public List<TutorSessionResponseDTO> findByMentorAndDate(LocalDate date, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findByMentorAndDate(mentor, date).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByMentorAndStudent(String studentId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        
        return sessionRepository.findByMentor(mentor).stream()
                .filter(session -> session.getStudent() != null && session.getStudent().getId().equals(studentId))
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TutorSessionResponseDTO create(TutorSessionCreateDTO dto, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        TutorSession session = tutorSessionMapper.toEntity(dto);
        session.setMentor(mentor);
        session.setDateHour(dto.getDateHour());

        return tutorSessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    public TutorSessionResponseDTO createFromStudent(TutorSessionCreateDTO dto) {

        TutorSession session = tutorSessionMapper.toEntity(dto);
        User mentor = userService.findById(dto.getMentorId()).orElseThrow();
        session.setMentor(mentor);
        session.setDateHour(dto.getDateHour());
        session.setDate(dto.getDate());
        
        return tutorSessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    public TutorSessionResponseDTO update(String id, TutorSessionCreateDTO dto, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        TutorSession session = sessionRepository.findById(id)
                .filter(s -> s.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
        
        session.setTitle(dto.getTitle());
        session.setType(dto.getType());
        session.setLink(dto.getLink());
        
        session.setDateHour(dto.getDateHour());
        session.setDate(dto.getDate());
        
        if (dto.getStudentId() != null && !dto.getStudentId().isEmpty()) {
            User student = userRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            session.setStudent(student);
        }
        
        return tutorSessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    public void delete(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        TutorSession session = sessionRepository.findById(id)
                .filter(s -> s.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
        
        sessionRepository.delete(session);
    }

      public void deleteStudentTutorSession(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        TutorSession session = sessionRepository.findById(id)
                .filter(s -> s.getStudent() != null && s.getStudent().getId().equals(student.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
        
        sessionRepository.delete(session);
    }
}