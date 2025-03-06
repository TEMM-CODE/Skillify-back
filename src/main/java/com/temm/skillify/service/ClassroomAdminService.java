package com.temm.skillify.service;


import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomAccessTokenRepository;
import com.temm.skillify.repository.ClassroomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomAdminService {
    
    private final ClassroomRepository classroomRepository;
    private final ClassroomAccessTokenRepository tokenRepository;
    
    public List<Classroom> findAllClassrooms() {
        return classroomRepository.findAll();
    }
    
    public Optional<Classroom> findClassroomById(String id) {
        return classroomRepository.findById(id);
    }
    
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }
    
    public Classroom updateClassroom(String id, Classroom updatedClassroom) {
        Classroom existingClassroom = classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + id));
        
        existingClassroom.setName(updatedClassroom.getName());
        
        if (updatedClassroom.getMentor() != null) {
            existingClassroom.setMentor(updatedClassroom.getMentor());
        }
        
        if (updatedClassroom.getStudents() != null) {
            existingClassroom.setStudents(updatedClassroom.getStudents());
        }
        
        return classroomRepository.save(existingClassroom);
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
    
    public Classroom updateClassroomMentor(String classroomId, User mentor) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        
        classroom.setMentor(mentor);
        return classroomRepository.save(classroom);
    }
    
    public Classroom updateClassroomStudents(String classroomId, Set<User> students) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + classroomId));
        
        classroom.setStudents(students);
        return classroomRepository.save(classroom);
    }
}