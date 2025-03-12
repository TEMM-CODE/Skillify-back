package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.QuestionMapper;
import com.temm.skillify.repository.QuestionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionAdminService {
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final OptionAdminService optionService;
    private final QuestionMapper questionMapper;
    
    public List<QuestionResponseDTO> findAll() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<QuestionResponseDTO> findById(String id) {
        return questionRepository.findById(id)
                .map(questionMapper::toResponseDTO);
    }
    
    public List<QuestionResponseDTO> findByMentorId(String mentorId) {
        User mentor = userService.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found with ID: " + mentorId));
        
        return questionRepository.findByMentor(mentor).stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public QuestionResponseDTO create(QuestionCreateDTO questionCreateDTO) {
        User mentor = userService.findById(questionCreateDTO.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found with ID: " + questionCreateDTO.getMentorId()));
        
        Question question = new Question();
        question.setTitle(questionCreateDTO.getTitle());
        question.setMentor(mentor);
        question.setOptions(new HashSet<>());
        
        Question savedQuestion = questionRepository.save(question);
        
        return questionMapper.toResponseDTO(savedQuestion);
    }
    
    @Transactional
    public Optional<QuestionResponseDTO> update(String id, QuestionCreateDTO questionCreateDTO) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    User mentor = userService.findById(questionCreateDTO.getMentorId())
                            .orElseThrow(() -> new RuntimeException("Mentor not found with ID: " + questionCreateDTO.getMentorId()));
                    
                    existingQuestion.setTitle(questionCreateDTO.getTitle());
                    existingQuestion.setMentor(mentor);
                    
                    Question updatedQuestion = questionRepository.save(existingQuestion);
                    return questionMapper.toResponseDTO(updatedQuestion);
                });
    }
    
    @Transactional
    public boolean deleteById(String id) {
        if (questionRepository.existsById(id)) {
            // First delete all associated options
            Question question = questionRepository.findById(id).orElse(null);
            if (question != null && question.getOptions() != null) {
                for (Option option : question.getOptions()) {
                    optionService.deleteById(option.getId());
                }
            }
            
            // Then delete the question
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}