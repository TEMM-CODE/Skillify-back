package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.TutorSessionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorSessionService {

    private final TutorSessionRepository sessionRepository;
    private final ClassroomService classroomService;

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
}