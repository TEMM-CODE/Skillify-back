package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.GoalExecutionResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.GoalExecutionMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.GoalExecutionRepository;
import com.temm.skillify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalExecutionStudentService {

    @Autowired
    private GoalExecutionRepository goalExecutionRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalExecutionMapper goalExecutionMapper;

    public List<GoalExecutionResponseDTO> getAllGoalExecutionsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);

        List<GoalExecution> goalExecutions = goalExecutionRepository.findByStudent(currentUser).stream()
                .filter(execution -> execution.getGoal().getClassrooms().stream()
                        .anyMatch(enrolledClassrooms::contains))
                .collect(Collectors.toList());

        return goalExecutionMapper.toResponseDTOList(goalExecutions);
    }

    public GoalExecutionResponseDTO getGoalExecutionById(String executionId) throws Exception {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);

        Optional<GoalExecution> executionOptional = goalExecutionRepository.findById(executionId);

        if (executionOptional.isEmpty()) {
            throw new Exception("Goal execution not found");
        }

        GoalExecution execution = executionOptional.get();

        boolean hasAccess = execution.getGoal().getClassrooms().stream()
                .anyMatch(enrolledClassrooms::contains);

        if (!hasAccess) {
            throw new Exception("You don't have access to this goal execution");
        }

        if (!execution.getStudent().getId().equals(currentUser.getId())) {
            throw new Exception("You can only access your own goal executions");
        }

        return goalExecutionMapper.toResponseDTO(execution);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}