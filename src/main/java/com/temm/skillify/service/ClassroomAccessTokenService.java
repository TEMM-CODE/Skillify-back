package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.repository.ClassroomAccessTokenRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomAccessTokenService {

    private final ClassroomAccessTokenRepository tokenRepository;
    private final ClassroomService classroomService;

    public List<ClassroomAccessToken> findAll() {
        return tokenRepository.findAll();
    }

    public List<ClassroomAccessToken> findByClassroomId(String classroomId) {
        Classroom classroom = classroomService.findById(classroomId).orElseThrow();
        return tokenRepository.findByClassroom(classroom);
    }

    public Optional<ClassroomAccessToken> findById(String id) {
        return tokenRepository.findById(id);
    }

    public Optional<ClassroomAccessToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public ClassroomAccessToken save(ClassroomAccessToken token) {
        return tokenRepository.save(token);
    }

    public ClassroomAccessToken generateToken(String classroomId) {
        Classroom classroom = classroomService.findById(classroomId).orElseThrow();
        ClassroomAccessToken token = new ClassroomAccessToken();
        token.setClassroom(classroom);
        token.setToken(UUID.randomUUID().toString());
        return tokenRepository.save(token);
    }

    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(t -> tokenRepository.deleteById(t.getId()));
    }
}