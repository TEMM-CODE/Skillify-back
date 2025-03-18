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

    /**
     * Create a new goal for specified classrooms
     * @param goalRequestDTO The goal details
     * @return The created goal
     */
    public GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO) {
        User mentor = getCurrentUser();
        
        // Verify mentor has access to all classrooms in the request
        validateMentorClassroomAccess(mentor, goalRequestDTO.getClassroomIds());
        
        Goal goal = goalMapper.toEntity(goalRequestDTO);
        Goal savedGoal = goalRepository.save(goal);
        return goalMapper.toResponseDTO(savedGoal);
    }
    
    /**
     * Update an existing goal
     * @param goalId The ID of the goal to update
     * @param goalRequestDTO The updated goal details
     * @return The updated goal
     * @throws Exception if the goal doesn't exist or the mentor doesn't have access
     */
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
    
    /**
     * Delete a goal
     * @param goalId The ID of the goal to delete
     * @throws Exception if the goal doesn't exist or the mentor doesn't have access
     */
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
    
    /**
     * Get all goals for a mentor (based on their mentored classrooms)
     * @return List of all goals for the current mentor
     */
    public List<GoalResponseDTO> getAllGoalsForCurrentMentor() {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = mentor.getMentoredClassrooms();
        
        List<Goal> allGoals = mentoredClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(allGoals);
    }
    
    /**
     * Get active goals for a mentor (based on their mentored classrooms)
     * @return List of active goals for the current mentor
     */
    public List<GoalResponseDTO> getActiveGoalsForCurrentMentor() {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = mentor.getMentoredClassrooms();
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> activeGoals = mentoredClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isBefore(now) && goal.getFinalDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(activeGoals);
    }
    
    /**
     * Get a specific goal by ID if the mentor has access to it
     * @param goalId The ID of the goal to retrieve
     * @return The goal if the mentor has access to it
     * @throws Exception if the goal doesn't exist or the mentor doesn't have access
     */
    public GoalResponseDTO getGoalById(String goalId) throws Exception {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = mentor.getMentoredClassrooms();
        
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
    
    /**
     * Get goals by type for a mentor
     * @param type The goal type
     * @return List of goals of the specified type for the current mentor
     */
    public List<GoalResponseDTO> getGoalsByTypeForCurrentMentor(GoalType type) {
        User mentor = getCurrentUser();
        List<Classroom> mentoredClassrooms = mentor.getMentoredClassrooms();
        
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
        List<Classroom> mentoredClassrooms = mentor.getMentoredClassrooms();
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