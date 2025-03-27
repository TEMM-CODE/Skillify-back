package com.temm.skillify.service;


import com.temm.skillify.model.dto.request.GoalRequestDTO;
import com.temm.skillify.model.dto.response.GoalResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.GoalType;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.GoalRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.model.mapper.GoalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalMentorService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalMapper goalMapper;

    public GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO) {
        User mentor = getCurrentUser();
        
        // Verify mentor has access to all classrooms in the request
        validateMentorClassroomAccess(mentor, goalRequestDTO.getClassroomIds());
        
        Goal goal = goalMapper.toEntity(goalRequestDTO);
        Goal savedGoal = goalRepository.save(goal);
        return goalMapper.toResponseDTO(savedGoal);
    }
    
    public GoalResponseDTO updateGoal(String goalId, GoalRequestDTO goalRequestDTO) throws Exception {
        User mentor = getCurrentUser();
        
        // Verify mentor has access to all classrooms in the request
        validateMentorClassroomAccess(mentor, goalRequestDTO.getClassroomIds());
        
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        
        if (goalOptional.isEmpty()) {
            throw new Exception("Goal not found");
        }
        
        Goal goal = goalOptional.get();
        
        // Verify mentor has access to modify this goal
        boolean hasAccess = goal.getClassrooms().stream()
            .anyMatch(classroom -> classroom.getMentor().equals(mentor));
            
        if (!hasAccess) {
            throw new Exception("You don't have permission to update this goal");
        }
        
        goalMapper.updateEntityFromDTO(goalRequestDTO, goal);
        Goal updatedGoal = goalRepository.save(goal);
        return goalMapper.toResponseDTO(updatedGoal);
    }
    
    public void deleteGoal(String goalId) throws Exception {
        User mentor = getCurrentUser();
        
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        
        if (goalOptional.isEmpty()) {
            throw new Exception("Goal not found");
        }
        
        Goal goal = goalOptional.get();
        
        // Verify mentor has access to delete this goal
        boolean hasAccess = goal.getClassrooms().stream()
            .anyMatch(classroom -> classroom.getMentor().equals(mentor));
            
        if (!hasAccess) {
            throw new Exception("You don't have permission to delete this goal");
        }
        
        goalRepository.delete(goal);
    }
    
    public List<GoalResponseDTO> getAllGoalsForCurrentMentor() {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = classroomRepository.findByMentor(mentor);
        
        List<Goal> allGoals = mentoredClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(allGoals);
    }
    
    public List<GoalResponseDTO> getActiveGoalsForCurrentMentor() {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = classroomRepository.findByMentor(mentor);
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> activeGoals = mentoredClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isBefore(now) && goal.getFinalDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(activeGoals);
    }
    
    public GoalResponseDTO getGoalById(String goalId) throws Exception {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = classroomRepository.findByMentor(mentor);
        
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        
        if (goalOptional.isEmpty()) {
            throw new Exception("Goal not found");
        }
        
        Goal goal = goalOptional.get();
        
        // Check if any of the mentor's classrooms are associated with this goal
        boolean hasAccess = goal.getClassrooms().stream()
            .anyMatch(mentoredClassrooms::contains);
            
        if (!hasAccess) {
            throw new Exception("You don't have access to this goal");
        }
        
        return goalMapper.toResponseDTO(goal);
    }
    
    public List<GoalResponseDTO> getGoalsByTypeForCurrentMentor(GoalType type) {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = classroomRepository.findByMentor(mentor);
        
        List<Goal> typeGoals = mentoredClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getType() == type)
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(typeGoals);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
    
    private void validateMentorClassroomAccess(User mentor, List<String> classroomIds) {
        List<Classroom> mentoredClassrooms = classroomRepository.findByMentor(mentor);
        List<String> mentoredClassroomIds = mentoredClassrooms.stream()
            .map(Classroom::getId)
            .collect(Collectors.toList());
            
        boolean hasAccessToAll = classroomIds.stream()
            .allMatch(mentoredClassroomIds::contains);
            
        if (!hasAccessToAll) {
            throw new RuntimeException("You don't have access to one or more of the specified classrooms");
        }
    }
}