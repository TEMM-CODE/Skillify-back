package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.repository.PracticeRepository;
import com.temm.skillify.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionStudentService {

    private final QuestionRepository questionRepository;
    private final PracticeRepository practiceRepository;

    public List<QuestionResponseDTO> getAllAvailableQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::mapToQuestionResponseDTO)
                .collect(Collectors.toList());
    }

    public QuestionResponseDTO getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + id));
        return mapToQuestionResponseDTO(question);
    }

    public List<QuestionResponseDTO> getQuestionsByPractice(String practiceId) {
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new EntityNotFoundException("Practice not found with id: " + practiceId));
        
        return practice.getQuestions().stream()
                .map(this::mapToQuestionResponseDTO)
                .collect(Collectors.toList());
    }

    private QuestionResponseDTO mapToQuestionResponseDTO(Question question) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        
        // Map mentor information
        UserResponseDTO mentorDTO = new UserResponseDTO();
        mentorDTO.setId(question.getMentor().getId());
        mentorDTO.setName(question.getMentor().getName());
        mentorDTO.setEmail(question.getMentor().getEmail());
        dto.setMentor(mentorDTO);
        
        // Map options
        Set<OptionResponseDTO> optionDTOs = question.getOptions().stream()
                .map(option -> {
                    OptionResponseDTO optionDTO = new OptionResponseDTO();
                    optionDTO.setId(option.getId());
                    optionDTO.setTitle(option.getTitle());
                    // For security reasons, we don't send the correct answer to students
                    optionDTO.setCorrect(null);
                    optionDTO.setCreatedAt(option.getCreatedAt());
                    optionDTO.setUpdatedAt(option.getUpdatedAt());
                    return optionDTO;
                })
                .collect(Collectors.toSet());
        
        dto.setOptions(optionDTOs);
        
        return dto;
    }
}