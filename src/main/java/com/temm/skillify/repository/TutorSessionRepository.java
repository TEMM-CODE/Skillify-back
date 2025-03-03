package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TutorSessionRepository extends JpaRepository<TutorSession, String> {
    List<TutorSession> findByMentor(User mentor);
    List<TutorSession> findByStudent(User student);
    List<TutorSession> findByMentorAndDate(User mentor, LocalDate date);
    List<TutorSession> findByStudentAndDate(User student, LocalDate date);
}