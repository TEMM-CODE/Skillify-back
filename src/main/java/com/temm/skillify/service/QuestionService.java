package com.temm.skillify.service;

import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.OptionRepository;
import com.temm.skillify.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserService userService;

    public List<Question> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return questionRepository.findByMentor(mentor);
    }

    public Question findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return questionRepository.findById(id)
                .filter(question -> question.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
    }

    public Question create(Question question, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        question.setMentor(mentor);
        
        if (question.getOptions() == null) {
            question.setOptions(new HashSet<>());
        }
        
        Question savedQuestion = questionRepository.save(question);
        
        // Save options if present and set the question reference
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            for (Option option : question.getOptions()) {
                option.setQuestion(savedQuestion);
                optionRepository.save(option);
            }
        }
        
        return savedQuestion;
    }

    public Question update(String id, Question updatedQuestion, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(id)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        question.setTitle(updatedQuestion.getTitle());
        
        return questionRepository.save(question);
    }

    public void delete(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(id)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        // Delete all options associated with this question
        List<Option> options = optionRepository.findByQuestion(question);
        optionRepository.deleteAll(options);
        
        questionRepository.delete(question);
    }

    public Question addOptions(String questionId, Set<Option> options, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        for (Option option : options) {
            option.setQuestion(question);
            optionRepository.save(option);
        }
        
        // Refresh the question to get the updated options
        return questionRepository.findById(questionId).orElseThrow();
    }

    public Question updateOption(String questionId, String optionId, Option updatedOption, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        Option option = optionRepository.findById(optionId)
                .filter(o -> o.getQuestion().getId().equals(questionId))
                .orElseThrow(() -> new EntityNotFoundException("Option not found or doesn't belong to this question"));
        
        option.setTitle(updatedOption.getTitle());
        option.setCorrect(updatedOption.getCorrect());
        
        optionRepository.save(option);
        
        // Refresh the question to get the updated options
        return questionRepository.findById(questionId).orElseThrow();
    }

    public Question deleteOption(String questionId, String optionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        Option option = optionRepository.findById(optionId)
                .filter(o -> o.getQuestion().getId().equals(questionId))
                .orElseThrow(() -> new EntityNotFoundException("Option not found or doesn't belong to this question"));
        
        optionRepository.delete(option);
        
        // Refresh the question to get the updated options
        return questionRepository.findById(questionId).orElseThrow();
    }
}