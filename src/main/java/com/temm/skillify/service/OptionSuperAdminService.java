package com.temm.skillify.service;


import com.temm.skillify.model.dto.request.OptionCreateDTO;
import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.OptionMapper;
import com.temm.skillify.repository.OptionRepository;
import com.temm.skillify.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OptionSuperAdminService {
    
    @Autowired
    private OptionMapper optionMapper;
    
    @Autowired
    private OptionRepository optionRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    // Get current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // Validate if current user is a super admin
    private void validateSuperAdminRole(User user) {
        if (!UserRole.SUPERADMIN.equals(user.getRole())) {
            throw new SecurityException("Only super admins can perform this action");
        }
    }

    @Transactional
    public OptionResponseDTO createOption(OptionCreateDTO createDTO) {
        User currentUser = getCurrentUser();
        validateSuperAdminRole(currentUser);

        Question question = questionRepository.findById(createDTO.getQuestionId())
            .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        Option option = new Option();
        option.setQuestion(question);
        option.setTitle(createDTO.getTitle());
        option.setCorrect(createDTO.getCorrect());

        Option savedOption = optionRepository.save(option);
        return optionMapper.toResponseDTO(savedOption);
    }

    public OptionResponseDTO getOption(String optionId) {
        User currentUser = getCurrentUser();
        validateSuperAdminRole(currentUser);

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        return optionMapper.toResponseDTO(option);
    }

    public List<OptionResponseDTO> getOptionsByQuestion(String questionId) {
        User currentUser = getCurrentUser();
        validateSuperAdminRole(currentUser);

        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        return question.getOptions().stream()
            .map(optionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public OptionResponseDTO updateOption(String optionId, OptionCreateDTO updateDTO) {
        User currentUser = getCurrentUser();
        validateSuperAdminRole(currentUser);

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        option.setTitle(updateDTO.getTitle());
        option.setCorrect(updateDTO.getCorrect());

        Option updatedOption = optionRepository.save(option);
        return optionMapper.toResponseDTO(updatedOption);
    }

    @Transactional
    public void deleteOption(String optionId) {
        User currentUser = getCurrentUser();
        validateSuperAdminRole(currentUser);

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        optionRepository.delete(option);
    }
}