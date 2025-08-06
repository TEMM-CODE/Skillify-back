package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.PracticeExecution;
import com.temm.skillify.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PracticeExecutionMapper {

    private final UserMapper userMapper;
    private final PracticeMapper practiceMapper;
    private final OptionMapper optionMapper;

    public PracticeExecutionResponseDTO toResponseDTO(PracticeExecution execution) {
        if (execution == null) {
            return null;
        }

        PracticeExecutionResponseDTO dto = new PracticeExecutionResponseDTO();
        dto.setId(execution.getId());
        dto.setStudent(userMapper.toResponseDTO(execution.getStudent()));
        dto.setPractice(practiceMapper.toResponseDTO(execution.getPractice()));
        dto.setSelectedAnswers(toOptionResponseDTOSet(execution.getSelectedAnswers()));
        dto.setCorrectAnswers(execution.getCorrectAnswers());
        dto.setDuration(execution.getDuration());  // Added duration mapping
        dto.setCreatedAt(execution.getCreatedAt());
        dto.setUpdatedAt(execution.getUpdatedAt());

        return dto;
    }

    public PracticeExecution toEntity(PracticeExecutionCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        PracticeExecution execution = new PracticeExecution();
        execution.setCorrectAnswers(dto.getCorrectAnswers());
        execution.setDuration(dto.getDuration());  // Added duration mapping
        
        // Set student
        if (dto.getStudentId() != null) {
            User student = new User();
            student.setId(dto.getStudentId());
            execution.setStudent(student);
        }

        // Note: SelectedAnswers should be set separately in service layer with full Option objects
        
        return execution;
    }

    public void updateEntityFromDTO(PracticeExecution execution, PracticeExecutionCreateDTO dto) {
        if (execution == null || dto == null) {
            return;
        }

        execution.setCorrectAnswers(dto.getCorrectAnswers());
        execution.setDuration(dto.getDuration());  // Added duration mapping
        
        // Set student
        if (dto.getStudentId() != null) {
            User student = new User();
            student.setId(dto.getStudentId());
            execution.setStudent(student);
        }

        // Set practice
        if (dto.getPracticeId() != null) {
            Practice practice = new Practice();
            practice.setId(dto.getPracticeId());
            execution.setPractice(practice);
        }
        
        // Note: SelectedAnswers should be updated separately in service layer
    }

    private Set<OptionResponseDTO> toOptionResponseDTOSet(Set<Option> options) {
        if (options == null) {
            return null;
        }

        return options.stream()
                .map(optionMapper::toResponseDTO)
                .collect(Collectors.toSet());
    }

    public List<PracticeExecutionResponseDTO> toResponseDTOList(List<PracticeExecution> executions) {
        if (executions == null) {
            return null;
        }
        return executions.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}