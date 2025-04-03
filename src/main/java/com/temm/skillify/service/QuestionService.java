package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.QuestionMapper;
import com.temm.skillify.repository.CourseRepository;
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
    private final CourseRepository courseRepository;
    private final QuestionMapper questionMapper;

    public List<QuestionResponseDTO> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        List<Question> questions = questionRepository.findByMentor(mentor);
        return questions.stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionResponseDTO> findAllBySuperAdmin(Authentication authentication) {
        // Ensure user is authenticated
        User currentUser = userService.getUserFromAuthentication(authentication);
        
        // Find all questions where the mentor has SUPERADMIN role
        List<Question> superAdminQuestions = questionRepository.findAll().stream()
                .filter(question -> question.getMentor() != null && 
                              question.getMentor().getRole() == UserRole.SUPERADMIN)
                .collect(Collectors.toList());
        
        // Convert to DTOs and return
        return superAdminQuestions.stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public QuestionResponseDTO findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        Question question = questionRepository.findById(id)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        return questionMapper.toResponseDTO(question);
    }

    public QuestionResponseDTO create(QuestionCreateDTO questionDTO, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setMentor(mentor);
        question.setOptions(new HashSet<>());
        
        // Check and set course if provided
        if (questionDTO.getCourseId() != null && !questionDTO.getCourseId().isEmpty()) {
            Course course = courseRepository.findById(questionDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + questionDTO.getCourseId()));
            question.setCourse(course);
        }
        
        // Set superAdminTypes if present
        if (questionDTO.getSuperAdminTypes() != null && !questionDTO.getSuperAdminTypes().isEmpty()) {
            question.setSuperAdminTypes(questionDTO.getSuperAdminTypes());
        }
        
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toResponseDTO(savedQuestion);
    }

    public QuestionResponseDTO update(String id, QuestionCreateDTO questionDTO, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(id)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        question.setTitle(questionDTO.getTitle());
        
        // Update course if provided
        if (questionDTO.getCourseId() != null && !questionDTO.getCourseId().isEmpty()) {
            Course course = courseRepository.findById(questionDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + questionDTO.getCourseId()));
            question.setCourse(course);
        } else {
            question.setCourse(null); // Allow removing course by sending null/empty courseId
        }
        
        // Update superAdminTypes if present
        if (questionDTO.getSuperAdminTypes() != null) {
            question.setSuperAdminTypes(questionDTO.getSuperAdminTypes());
        }
        
        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toResponseDTO(updatedQuestion);
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

    public QuestionResponseDTO addOptions(String questionId, Set<Option> options, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        for (Option option : options) {
            option.setQuestion(question);
            optionRepository.save(option);
        }
        
        // Refresh the question to get the updated options
        Question updatedQuestion = questionRepository.findById(questionId).orElseThrow();
        return questionMapper.toResponseDTO(updatedQuestion);
    }

    public QuestionResponseDTO updateOption(String questionId, String optionId, Option updatedOption, Authentication authentication) {
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
        Question refreshedQuestion = questionRepository.findById(questionId).orElseThrow();
        return questionMapper.toResponseDTO(refreshedQuestion);
    }

    public QuestionResponseDTO deleteOption(String questionId, String optionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        Option option = optionRepository.findById(optionId)
                .filter(o -> o.getQuestion().getId().equals(questionId))
                .orElseThrow(() -> new EntityNotFoundException("Option not found or doesn't belong to this question"));
        
        optionRepository.delete(option);
        
        // Refresh the question to get the updated options
        Question refreshedQuestion = questionRepository.findById(questionId).orElseThrow();
        return questionMapper.toResponseDTO(refreshedQuestion);
    }
}