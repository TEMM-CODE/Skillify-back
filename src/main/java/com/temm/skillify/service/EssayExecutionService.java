package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayExecutionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayExecutionService {

    private final EssayExecutionRepository essayExecutionRepository;
    private final UserService userService;

    public List<EssayExecution> findAll() {
        return essayExecutionRepository.findAll();
    }

    public List<EssayExecution> findAllByStudentEmail(String email) {
        User student = userService.findByEmail(email).orElseThrow();
        return essayExecutionRepository.findByStudent(student);
    }

    public Optional<EssayExecution> findById(String id) {
        return essayExecutionRepository.findById(id);
    }

    public Optional<EssayExecution> findByIdAndStudentEmail(String id, String email) {
        User student = userService.findByEmail(email).orElseThrow();
        return essayExecutionRepository.findByIdAndStudent(id, student);
    }

    public EssayExecution save(EssayExecution essayExecution) {
        return essayExecutionRepository.save(essayExecution);
    }

    public EssayExecution saveForStudent(EssayExecution essayExecution, String studentEmail) {
        User student = userService.findByEmail(studentEmail).orElseThrow();
        essayExecution.setStudent(student);
        return essayExecutionRepository.save(essayExecution);
    }

    public void deleteById(String id) {
        essayExecutionRepository.deleteById(id);
    }
}