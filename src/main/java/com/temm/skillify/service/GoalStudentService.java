package com.temm.skillify.service;



import com.temm.skillify.model.dto.response.GoalResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.User;
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
public class GoalStudentService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalMapper goalMapper;

    /**
     * Get all active goals for a student (based on their enrolled classrooms)
     * @return List of active goals for the current student
     */
    public List<GoalResponseDTO> getActiveGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = currentUser.getEnrolledClassrooms();
        
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> activeGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isBefore(now) && goal.getFinalDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(activeGoals);
    }
    
    /**
     * Get all goals for a student (based on their enrolled classrooms)
     * @return List of all goals for the current student
     */
    public List<GoalResponseDTO> getAllGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = currentUser.getEnrolledClassrooms();
        
        List<Goal> allGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(allGoals);
    }
    
    /**
     * Get a specific goal by ID if the student has access to it
     * @param goalId The ID of the goal to retrieve
     * @return The goal if the student has access to it
     * @throws Exception if the goal doesn't exist or the student doesn't have access
     */
    public GoalResponseDTO getGoalById(String goalId) throws Exception {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = currentUser.getClassroom();
        
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        
        if (goalOptional.isEmpty()) {
            throw new Exception("Goal not found");
        }
        
        Goal goal = goalOptional.get();
        
        // Check if any of the student's enrolled classrooms are associated with this goal
        boolean hasAccess = goal.getClassrooms().stream()
            .anyMatch(enrolledClassrooms::contains);
            
        if (!hasAccess) {
            throw new Exception("You don't have access to this goal");
        }
        
        return goalMapper.toResponseDTO(goal);
    }
    
    /**
     * Get upcoming goals for a student (opening in the future)
     * @return List of upcoming goals for the current student
     */
    public List<GoalResponseDTO> getUpcomingGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = currentUser.getEnrolledClassrooms();
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> upcomingGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(upcomingGoals);
    }
    
    /**
     * Get past goals for a student (final date has passed)
     * @return List of past goals for the current student
     */
    public List<GoalResponseDTO> getPastGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = currentUser.getEnrolledClassrooms();
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> pastGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getFinalDate().isBefore(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(pastGoals);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
