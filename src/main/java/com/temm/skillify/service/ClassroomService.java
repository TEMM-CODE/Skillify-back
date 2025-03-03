package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserService userService;

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
}