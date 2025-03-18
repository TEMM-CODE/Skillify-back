package com.temm.skillify.controller.student;


import com.temm.skillify.model.dto.response.GoalResponseDTO;
import com.temm.skillify.service.GoalStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/goals")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class GoalStudentController {

    @Autowired
    private GoalStudentService goalStudentService;

    /**
     * Get all goals for the current student
     */
    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        List<GoalResponseDTO> goals = goalStudentService.getAllGoalsForCurrentStudent();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get active goals for the current student
     */
    @GetMapping("/active")
    public ResponseEntity<List<GoalResponseDTO>> getActiveGoals() {
        List<GoalResponseDTO> goals = goalStudentService.getActiveGoalsForCurrentStudent();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get upcoming goals for the current student
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<GoalResponseDTO>> getUpcomingGoals() {
        List<GoalResponseDTO> goals = goalStudentService.getUpcomingGoalsForCurrentStudent();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get past goals for the current student
     */
    @GetMapping("/past")
    public ResponseEntity<List<GoalResponseDTO>> getPastGoals() {
        List<GoalResponseDTO> goals = goalStudentService.getPastGoalsForCurrentStudent();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get a specific goal by ID for the current student
     */
    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponseDTO> getGoalById(@PathVariable String goalId) {
        try {
            GoalResponseDTO goal = goalStudentService.getGoalById(goalId);
            return ResponseEntity.ok(goal);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}