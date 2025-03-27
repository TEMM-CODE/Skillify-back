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

    public List<GoalResponseDTO> getActiveGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);
        
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> activeGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isBefore(now) && goal.getFinalDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(activeGoals);
    }
    
    public List<GoalResponseDTO> getAllGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);
        
        List<Goal> allGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(allGoals);
    }
    
    public GoalResponseDTO getGoalById(String goalId) throws Exception {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);
        
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        
        if (goalOptional.isEmpty()) {
            throw new Exception("Goal not found");
        }
        
        Goal goal = goalOptional.get();
        
        boolean hasAccess = goal.getClassrooms().stream()
            .anyMatch(enrolledClassrooms::contains);
            
        if (!hasAccess) {
            throw new Exception("You don't have access to this goal");
        }
        
        return goalMapper.toResponseDTO(goal);
    }
    
    public List<GoalResponseDTO> getUpcomingGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);
        LocalDateTime now = LocalDateTime.now();
        
        List<Goal> upcomingGoals = enrolledClassrooms.stream()
            .flatMap(classroom -> goalRepository.findByClassroomsContaining(classroom).stream())
            .filter(goal -> goal.getOpeningDate().isAfter(now))
            .distinct()
            .collect(Collectors.toList());
            
        return goalMapper.toResponseDTOList(upcomingGoals);
    }
    
    public List<GoalResponseDTO> getPastGoalsForCurrentStudent() {
        User currentUser = getCurrentUser();
        List<Classroom> enrolledClassrooms = classroomRepository.findByStudentsContaining(currentUser);
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