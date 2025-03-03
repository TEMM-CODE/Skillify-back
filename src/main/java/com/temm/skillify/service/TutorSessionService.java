package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
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

    public List<TutorSession> findAll() {
        return sessionRepository.findAll();
    }

    public List<TutorSession> findByMentor(User mentor) {
        return sessionRepository.findByMentor(mentor);
    }

    public List<TutorSession> findByStudent(User student) {
        return sessionRepository.findByStudent(student);
    }

    public List<TutorSession> findByMentorAndDate(User mentor, LocalDate date) {
        return sessionRepository.findByMentorAndDate(mentor, date);
    }

    public Optional<TutorSession> findById(String id) {
        return sessionRepository.findById(id);
    }

    public TutorSession save(TutorSession session) {
        return sessionRepository.save(session);
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

     public List<TutorSession> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findByMentor(mentor);
    }

    public TutorSession findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findById(id)
                .filter(session -> session.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
    }

    public List<TutorSession> findByMentorAndDate(LocalDate date, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return sessionRepository.findByMentorAndDate(mentor, date);
    }

    public List<TutorSession> findByMentorAndStudent(String studentId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        
        return sessionRepository.findByMentor(mentor).stream()
                .filter(session -> session.getStudent() != null && session.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }

    public TutorSession create(TutorSession session, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        session.setMentor(mentor);
        
        if (session.getStudent() != null) {
            User student = userRepository.findById(session.getStudent().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            session.setStudent(student);
        }
        
        // If dateHour is set but date is not, extract date from dateHour
        if (session.getDateHour() != null && session.getDate() == null) {
            session.setDate(session.getDateHour().toLocalDate());
        }
        
        return sessionRepository.save(session);
    }

    public TutorSession update(String id, TutorSession updatedSession, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        TutorSession session = sessionRepository.findById(id)
                .filter(s -> s.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
        
        session.setTitle(updatedSession.getTitle());
        session.setType(updatedSession.getType());
        session.setLink(updatedSession.getLink());
        
        if (updatedSession.getDateHour() != null) {
            session.setDateHour(updatedSession.getDateHour());
            session.setDate(updatedSession.getDateHour().toLocalDate());
        } else if (updatedSession.getDate() != null) {
            session.setDate(updatedSession.getDate());
        }
        
        if (updatedSession.getStudent() != null) {
            User student = userRepository.findById(updatedSession.getStudent().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            session.setStudent(student);
        }
        
        return sessionRepository.save(session);
    }

    public void delete(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        TutorSession session = sessionRepository.findById(id)
                .filter(s -> s.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found or you don't have permission"));
        
        sessionRepository.delete(session);
    }

}