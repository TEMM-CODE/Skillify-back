package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.GoalExecutionResponseDTO;
import com.temm.skillify.service.GoalExecutionStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/goal-executions")
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class GoalExecutionStudentController {

    @Autowired
    private GoalExecutionStudentService goalExecutionStudentService;

    /**
     * Get all goal executions for the current student
     */
    @GetMapping
    public ResponseEntity<List<GoalExecutionResponseDTO>> getAllGoalExecutions() {
        List<GoalExecutionResponseDTO> executions = goalExecutionStudentService.getAllGoalExecutionsForCurrentStudent();
        return ResponseEntity.ok(executions);
    }

    /**
     * Get a specific goal execution by ID for the current student
     */
    @GetMapping("/{executionId}")
    public ResponseEntity<GoalExecutionResponseDTO> getGoalExecutionById(@PathVariable String executionId) {
        try {
            GoalExecutionResponseDTO execution = goalExecutionStudentService.getGoalExecutionById(executionId);
            return ResponseEntity.ok(execution);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}