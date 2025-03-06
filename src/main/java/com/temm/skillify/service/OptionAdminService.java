package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.repository.OptionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionAdminService {
    
    private final OptionRepository optionRepository;
    
    public List<Option> findAll() {
        return optionRepository.findAll();
    }
    
    public Optional<Option> findById(String id) {
        return optionRepository.findById(id);
    }
    
    public List<Option> findByQuestion(Question question) {
        return optionRepository.findByQuestion(question);
    }
    
    public List<Option> findByQuestionAndCorrect(Question question, Boolean correct) {
        return optionRepository.findByQuestionAndCorrect(question, correct);
    }
    
    public Option save(Option option) {
        return optionRepository.save(option);
    }
    
    public void deleteById(String id) {
        optionRepository.deleteById(id);
    }
}