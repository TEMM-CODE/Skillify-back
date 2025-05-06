package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.GoalExecutionCreateDTO;
import com.temm.skillify.model.dto.response.GoalExecutionResponseDTO;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.GoalRepository;
import com.temm.skillify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoalExecutionMapper {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalMapper goalMapper;

    @Autowired
    private UserMapper userMapper;

    public GoalExecution toEntity(GoalExecutionCreateDTO dto) {
        GoalExecution execution = new GoalExecution();

        // Map goal ID to Goal entity
        Goal goal = goalRepository.findById(dto.getGoalId())
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + dto.getGoalId()));
        execution.setGoal(goal);

        // Map student ID to User entity
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));
        execution.setStudent(student);

        execution.setAmount(dto.getAmount());

        return execution;
    }

    public GoalExecutionResponseDTO toResponseDTO(GoalExecution entity) {
        if (entity == null) {
            return null;
        }

        GoalExecutionResponseDTO dto = new GoalExecutionResponseDTO();

        dto.setId(entity.getId());
        dto.setGoal(goalMapper.toResponseDTO(entity.getGoal()));
        dto.setStudent(userMapper.toResponseDTO(entity.getStudent()));
        dto.setAmount(entity.getAmount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public List<GoalExecutionResponseDTO> toResponseDTOList(List<GoalExecution> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDTO(GoalExecutionCreateDTO dto, GoalExecution entity) {
        // Map goal ID to Goal entity
        Goal goal = goalRepository.findById(dto.getGoalId())
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + dto.getGoalId()));
        entity.setGoal(goal);

        // Map student ID to User entity
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));
        entity.setStudent(student);

        entity.setAmount(dto.getAmount());
    }
}