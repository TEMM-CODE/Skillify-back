package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.request.GoalRequestDTO;
import com.temm.skillify.model.dto.response.GoalResponseDTO;
import com.temm.skillify.model.enums.GoalType;
import com.temm.skillify.service.GoalMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/mentor/goals")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class GoalMentorController {

    @Autowired
    private GoalMentorService goalMentorService;

    /**
     * Create a new goal
     */
    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {
        try {
            GoalResponseDTO createdGoal = goalMentorService.createGoal(goalRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update an existing goal
     */
    @PutMapping("/{goalId}")
    public ResponseEntity<GoalResponseDTO> updateGoal(
            @PathVariable String goalId,
            @Valid @RequestBody GoalRequestDTO goalRequestDTO) {
        try {
            GoalResponseDTO updatedGoal = goalMentorService.updateGoal(goalId, goalRequestDTO);
            return ResponseEntity.ok(updatedGoal);
        } catch (Exception e) {
            if (e.getMessage().equals("Goal not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete a goal
     */
    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable String goalId) {
        try {
            goalMentorService.deleteGoal(goalId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage().equals("Goal not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all goals for the current mentor
     */
    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        List<GoalResponseDTO> goals = goalMentorService.getAllGoalsForCurrentMentor();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get active goals for the current mentor
     */
    @GetMapping("/active")
    public ResponseEntity<List<GoalResponseDTO>> getActiveGoals() {
        List<GoalResponseDTO> goals = goalMentorService.getActiveGoalsForCurrentMentor();
        return ResponseEntity.ok(goals);
    }
    
    /**
     * Get a specific goal by ID for the current mentor
     */
    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponseDTO> getGoalById(@PathVariable String goalId) {
        try {
            GoalResponseDTO goal = goalMentorService.getGoalById(goalId);
            return ResponseEntity.ok(goal);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get goals by type for the current mentor
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByType(@PathVariable GoalType type) {
        List<GoalResponseDTO> goals = goalMentorService.getGoalsByTypeForCurrentMentor(type);
        return ResponseEntity.ok(goals);
    }
}