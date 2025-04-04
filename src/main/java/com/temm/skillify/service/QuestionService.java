package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.QuestionContentCreateDTO;
import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.QuestionContent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.QuestionContentMapper;
import com.temm.skillify.model.mapper.QuestionMapper;
import com.temm.skillify.repository.CourseRepository;
import com.temm.skillify.repository.OptionRepository;
import com.temm.skillify.repository.QuestionContentRepository;
import com.temm.skillify.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final QuestionMapper questionMapper;
    private final QuestionContentRepository questionContentRepository;
    private final QuestionContentMapper questionContentMapper;

    public List<QuestionResponseDTO> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        List<Question> questions = questionRepository.findByMentor(mentor);
        return questions.stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionResponseDTO> findAllBySuperAdmin(Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        List<Question> superAdminQuestions = questionRepository.findAll().stream()
                .filter(question -> question.getMentor() != null && 
                              question.getMentor().getRole() == UserRole.SUPERADMIN)
                .collect(Collectors.toList());
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
        
        if (questionDTO.getCourseId() != null && !questionDTO.getCourseId().isEmpty()) {
            Course course = courseRepository.findById(questionDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + questionDTO.getCourseId()));
            question.setCourse(course);
        }
        
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
        
        if (questionDTO.getCourseId() != null && !questionDTO.getCourseId().isEmpty()) {
            Course course = courseRepository.findById(questionDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + questionDTO.getCourseId()));
            question.setCourse(course);
        } else {
            question.setCourse(null);
        }
        
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
        
        Question refreshedQuestion = questionRepository.findById(questionId).orElseThrow();
        return questionMapper.toResponseDTO(refreshedQuestion);
    }

    public QuestionResponseDTO updateQuestionContent(String questionId, List<QuestionContentCreateDTO> contentDTOs, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        // Fetch the question and verify ownership
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));

        // Get existing content
        List<QuestionContent> existingContent = question.getContent();
        
        // Create a map of existing content by position for easier lookup
        Map<Integer, QuestionContent> existingContentMap = existingContent.stream()
                .collect(Collectors.toMap(QuestionContent::getPosition, Function.identity()));

        // New content list to replace the old one
        List<QuestionContent> updatedContent = new ArrayList<>();

        // Process each DTO
        for (QuestionContentCreateDTO dto : contentDTOs) {
            QuestionContent content;
            if (existingContentMap.containsKey(dto.getPosition())) {
                // Update existing content
                content = existingContentMap.get(dto.getPosition());
                content.setType(dto.getType());
                content.setValue(dto.getValue());
            } else {
                // Create new content
                content = questionContentMapper.toEntity(dto, question);
            }
            updatedContent.add(content);
        }

        // Remove content that is no longer in the DTO list (orphan removal will handle deletion)
        existingContent.clear();
        existingContent.addAll(updatedContent);

        // Save the updated question (cascade will handle content persistence)
        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toResponseDTO(updatedQuestion);
    }
}