package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionAdminService {
    
    private final QuestionRepository questionRepository;
    
    public List<Question> findAll() {
        return questionRepository.findAll();
    }
    
    public Optional<Question> findById(String id) {
        return questionRepository.findById(id);
    }
    
    public List<Question> findByMentor(User mentor) {
        return questionRepository.findByMentor(mentor);
    }
    
    public Question save(Question question) {
        return questionRepository.save(question);
    }
    
    public void deleteById(String id) {
        questionRepository.deleteById(id);
    }
}