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
public class TutorSessionAdminService {
    
    private final TutorSessionRepository tutorSessionRepository;
    
    public List<TutorSession> findAll() {
        return tutorSessionRepository.findAll();
    }
    
    public Optional<TutorSession> findById(String id) {
        return tutorSessionRepository.findById(id);
    }
    
    public List<TutorSession> findByMentor(User mentor) {
        return tutorSessionRepository.findByMentor(mentor);
    }
    
    public List<TutorSession> findByStudent(User student) {
        return tutorSessionRepository.findByStudent(student);
    }
    
    public List<TutorSession> findByMentorAndDate(User mentor, LocalDate date) {
        return tutorSessionRepository.findByMentorAndDate(mentor, date);
    }
    
    public List<TutorSession> findByStudentAndDate(User student, LocalDate date) {
        return tutorSessionRepository.findByStudentAndDate(student, date);
    }
    
    public TutorSession save(TutorSession tutorSession) {
        return tutorSessionRepository.save(tutorSession);
    }
    
    public void deleteById(String id) {
        tutorSessionRepository.deleteById(id);
    }
}