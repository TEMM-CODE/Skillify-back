package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.OptionRepository;
import com.temm.skillify.repository.PracticeRepository;
import com.temm.skillify.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionStudentService {
    
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final PracticeRepository practiceRepository;
    private final UserService userService;
    private final MappingService mappingService;
    
    public List<OptionResponseDTO> getOptionsByQuestion(String questionId, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));
        
        // Check if the student has access to this question via a practice
        validateStudentAccessToQuestion(student, question);
        
        // Return options but without correct flag for student
        return optionRepository.findByQuestion(question).stream()
                .map(this::mapToProtectedOptionResponseDTO)
                .collect(Collectors.toList());
    }
    
    public OptionResponseDTO getOptionById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Option not found with id: " + id));
        
        // Check if the student has access to this option via a practice
        validateStudentAccessToQuestion(student, option.getQuestion());
        
        // Return the option but without correct flag for student
        return mapToProtectedOptionResponseDTO(option);
    }
    
    // Helper method to validate student's access to a question
    private void validateStudentAccessToQuestion(User student, Question question) {
        LocalDateTime now = LocalDateTime.now();
        
        // Find practices that include this question
        List<Practice> practicesWithQuestion = practiceRepository.findAll().stream()
                .filter(practice -> practice.getQuestions().contains(question))
                .collect(Collectors.toList());
        
        // Check if any of these practices are in classrooms where the student is enrolled
        boolean hasAccess = practicesWithQuestion.stream()
                .filter(practice -> practice.getOpeningDate().isBefore(now) && 
                                   practice.getMaximumDate().isAfter(now))
                .anyMatch(practice -> {
                    Classroom classroom = practice.getClassroom();
                    return classroom.getStudents().contains(student);
                });
        
        if (!hasAccess) {
            throw new EntityNotFoundException("Question not found or not accessible");
        }
    }
    
    // Create a DTO without revealing if the option is correct
    private OptionResponseDTO mapToProtectedOptionResponseDTO(Option option) {
        OptionResponseDTO dto = new OptionResponseDTO();
        dto.setId(option.getId());
        dto.setTitle(option.getTitle());
        dto.setCorrect(null); // Don't reveal if the option is correct to students
        dto.setCreatedAt(option.getCreatedAt());
        dto.setUpdatedAt(option.getUpdatedAt());
        return dto;
    }
}