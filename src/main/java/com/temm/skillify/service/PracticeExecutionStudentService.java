package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.PracticeExecutionCreateDTO;
import com.temm.skillify.model.dto.response.PracticeExecutionResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.PracticeExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.GoalType;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.PracticeExecutionMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.ExperienceEventRepository;
import com.temm.skillify.repository.GoalExecutionRepository;
import com.temm.skillify.repository.GoalRepository;
import com.temm.skillify.repository.PracticeExecutionRepository;
import com.temm.skillify.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeExecutionStudentService {

    private final PracticeExecutionRepository practiceExecutionRepository;
    private final PracticeRepository practiceRepository;
    private final PracticeExecutionMapper practiceExecutionMapper;
    private final UserService userService;
    private final GoalRepository goalRepository;
    private final GoalExecutionRepository goalExecutionRepository;
    private final ClassroomRepository classroomRepository;
    private final GamificationService gamificationService;
    private final ExperienceEventRepository experienceEventRepository;

    // Get current authenticated student
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // Validate if current user is a student
    private void validateStudentRole(User user) {
        if (!UserRole.ESTUDANTE.equals(user.getRole())) {
            throw new SecurityException("Only students can perform this action");
        }
    }

    // Validate if the current user is the student who authored the practice execution
    private void validateStudentOwnership(User currentStudent, PracticeExecution execution) {
        if (!Objects.equals(currentStudent.getId(), execution.getStudent().getId())) {
            throw new SecurityException("You can only access your own practice executions");
        }
    }

    @Transactional(readOnly = true)
    public List<PracticeExecutionResponseDTO> getAllStudentPracticeExecutions() {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        List<PracticeExecution> executions = practiceExecutionRepository.findByStudentId(currentStudent.getId());
        return practiceExecutionMapper.toResponseDTOList(executions);
    }

    @Transactional(readOnly = true)
    public PracticeExecutionResponseDTO getPracticeExecutionById(String id) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        PracticeExecution execution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateStudentOwnership(currentStudent, execution);
        return practiceExecutionMapper.toResponseDTO(execution);
    }

 @Transactional
    public PracticeExecutionResponseDTO createPracticeExecution(PracticeExecutionCreateDTO createDTO) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        // Ensure the studentId in DTO matches current user
        if (!Objects.equals(currentStudent.getId(), createDTO.getStudentId())) {
            throw new SecurityException("You can only create practice executions for yourself");
        }

        // Get the Practice entity
        Practice practice = practiceRepository.findById(createDTO.getPracticeId())
                .orElseThrow(() -> new RuntimeException("Practice not found with id: " + createDTO.getPracticeId()));

        // Count existing executions for this student and practice
        long existingExecutionsCount = practiceExecutionRepository
                .countByStudentIdAndPracticeId(currentStudent.getId(), createDTO.getPracticeId());

        // Check if numberOfAllowedAttempts is null (unlimited) or if we're within the limit
        Integer allowedAttempts = practice.getNumberOfAllowedAttempts();
        if (allowedAttempts != null && existingExecutionsCount >= allowedAttempts) {
            throw new IllegalStateException(
                    "Maximum number of attempts (" + allowedAttempts + ") reached for this practice");
        }

        PracticeExecution execution = practiceExecutionMapper.toEntity(createDTO);
        PracticeExecution savedExecution = practiceExecutionRepository.save(execution);

        int questionCount = practice.getQuestions() != null ? practice.getQuestions().size() : 0;
        if (questionCount > 0) {
            int xpAwarded = questionCount * GamificationService.EntityType.QUESTION.getXp();
            gamificationService.awardXp(currentStudent, xpAwarded);

            // Create and save ExperienceEvent
            ExperienceEvent experienceEvent = new ExperienceEvent();
            experienceEvent.setUser(currentStudent);
            experienceEvent.setXp(xpAwarded);
            experienceEventRepository.save(experienceEvent);
        }

        // 1) Find active (non-expired) goals by student
        LocalDateTime now = LocalDateTime.now();
        List<Classroom> studentClassrooms = classroomRepository.findByStudentsContaining(currentStudent);
        List<Goal> activeGoals = goalRepository.findByClassroomsInAndFinalDateAfter(
                studentClassrooms, now);

        // 2) Filter by type QUESTION
        List<Goal> questionGoals = activeGoals.stream()
                .filter(goal -> goal.getType() == GoalType.QUESTION)
                .collect(Collectors.toList());

        // Process each question goal
        for (Goal goal : questionGoals) {
            // 3) Find practice executions done during the goal's time period, one per distinct practice
            List<PracticeExecution> relevantExecutions = practiceExecutionRepository.findByStudentId(currentStudent.getId()).stream()
                    .filter(exec -> exec.getPractice().getClassroom().getId().equals(
                            goal.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toList()).get(0)))
                    .filter(exec -> exec.getCreatedAt().isAfter(goal.getOpeningDate())
                            && exec.getCreatedAt().isBefore(goal.getFinalDate()))
                    // Group by Practice and select the most recent execution
                    .collect(Collectors.groupingBy(
                            exec -> exec.getPractice(),
                            Collectors.maxBy(Comparator.comparing(PracticeExecution::getCreatedAt))
                    ))
                    .values()
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            // 4) Check if a goal execution already exists
            GoalExecution goalExecution = goalExecutionRepository.findByGoalAndStudent(goal, currentStudent)
                    .orElse(null);

            if (goalExecution == null) {
                // 5) Create new goal execution if it doesn't exist
                goalExecution = new GoalExecution();
                goalExecution.setGoal(goal);
                goalExecution.setStudent(currentStudent);
                // Set amount to the number of unique practice executions
                goalExecution.setAmount(relevantExecutions.size());
                goalExecutionRepository.save(goalExecution);
            } else {
                // 6) Update existing goal execution
                goalExecution.setAmount(goalExecution.getAmount() + 1);
                goalExecutionRepository.save(goalExecution);
            }
        }

        return practiceExecutionMapper.toResponseDTO(savedExecution);
    }

    @Transactional
    public PracticeExecutionResponseDTO updatePracticeExecution(String id, PracticeExecutionCreateDTO updateDTO) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        PracticeExecution existingExecution = practiceExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice execution not found with id: " + id));
        
        validateStudentOwnership(currentStudent, existingExecution);

        // Ensure the studentId in DTO matches current user
        if (!Objects.equals(currentStudent.getId(), updateDTO.getStudentId())) {
            throw new SecurityException("You can only update practice executions for yourself");
        }

        practiceExecutionMapper.updateEntityFromDTO(existingExecution, updateDTO);
        PracticeExecution updatedExecution = practiceExecutionRepository.save(existingExecution);
        return practiceExecutionMapper.toResponseDTO(updatedExecution);
    }
}