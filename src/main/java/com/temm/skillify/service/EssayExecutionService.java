package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.EssayExecutionCreateDTO;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.GoalType;
import com.temm.skillify.model.mapper.EssayExecutionMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.EssayExecutionRepository;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.repository.ExperienceEventRepository;
import com.temm.skillify.repository.GoalExecutionRepository;
import com.temm.skillify.repository.GoalRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayExecutionService {
    private final EssayExecutionRepository essayExecutionRepository;
    private final EssayExecutionMapper essayExecutionMapper;
    private final UserService userService;
    private final EssayRepository essayRepository;
    private final GoalRepository goalRepository;
    private final GoalExecutionRepository goalExecutionRepository;
    private final ClassroomRepository classroomRepository;
    private final GamificationService gamificationService;
    private final ExperienceEventRepository experienceEventRepository;

    public List<EssayExecutionResponseDTO> findAllDTOs() {
        List<EssayExecution> executions = essayExecutionRepository.findAll();
        return essayExecutionMapper.toResponseDTOList(executions);
    }

    public List<EssayExecutionResponseDTO> findAllDTOsByStudentEmail(String email) {
        User student = userService.findByEmail(email).orElseThrow(() -> 
            new RuntimeException("Student not found with email: " + email));
        List<EssayExecution> executions = essayExecutionRepository.findByStudent(student);
        return essayExecutionMapper.toResponseDTOList(executions);
    }

    public Optional<EssayExecutionResponseDTO> findDTOById(String id) {
        return essayExecutionRepository.findById(id)
                .map(essayExecutionMapper::toResponseDTO);
    }

    public Optional<EssayExecutionResponseDTO> findDTOByIdAndStudentEmail(String id, String email) {
        User student = userService.findByEmail(email).orElseThrow(() -> 
            new RuntimeException("Student not found with email: " + email));
        return essayExecutionRepository.findByIdAndStudent(id, student)
                .map(essayExecutionMapper::toResponseDTO);
    }

    public EssayExecutionResponseDTO save(EssayExecutionCreateDTO createDTO) {
        EssayExecution essayExecution = essayExecutionMapper.toEntity(createDTO);
        EssayExecution savedExecution = essayExecutionRepository.save(essayExecution);
        return essayExecutionMapper.toResponseDTO(savedExecution);
    }

   

    public EssayExecutionResponseDTO saveForStudent(EssayExecutionCreateDTO createDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = (User) authentication.getPrincipal();

        // Create entity from DTO
        EssayExecution essayExecution = new EssayExecution();
        essayExecution.setStudent(student);
        essayExecution.setText(createDTO.getText());

        // Set essay
        if (createDTO.getEssayId() != null) {
            essayExecution.setEssay(essayRepository.findById(createDTO.getEssayId())
                    .orElseThrow(() -> new RuntimeException("Essay not found")));
        }

        Essay essay = essayRepository.findById(createDTO.getEssayId()).orElseThrow();


        // Check minimum word count
        String text = createDTO.getText();
        int wordCount = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
        if (essay.getMinWords() != null && wordCount < essay.getMinWords()) {
            throw new RuntimeException("Essay text does not meet minimum word count of " + essay.getMinWords());
        }

        
        List<EssayExecution> executionAlready = essayExecutionRepository.findByEssay(essay);

        if(!executionAlready.isEmpty()){
            throw new RuntimeException("Já há um essay execution");
        }

        // Save the essay execution
        EssayExecution savedExecution = essayExecutionRepository.save(essayExecution);

        // Award XP for essay submission and create ExperienceEvent
        int xpAwarded = GamificationService.EntityType.ESSAY.getXp();
        gamificationService.awardXpForEntity(student, GamificationService.EntityType.ESSAY);

        // Create and save ExperienceEvent
        ExperienceEvent experienceEvent = new ExperienceEvent();
        experienceEvent.setUser(student);
        experienceEvent.setXp(xpAwarded);
        experienceEventRepository.save(experienceEvent);

        // 1) Find active (non-expired) goals by student
        LocalDateTime now = LocalDateTime.now();
        List<Classroom> studentClassrooms = classroomRepository.findByStudentsContaining(student);
        List<Goal> activeGoals = goalRepository.findByClassroomsInAndFinalDateAfter(
                studentClassrooms, now);
        System.out.println("Nao tem metas" + activeGoals.isEmpty());

        // 2) Filter by type ESSAY
        List<Goal> essayGoals = activeGoals.stream()
                .filter(goal -> goal.getType() == GoalType.ESSAY)
                .collect(Collectors.toList());

        // Process each essay goal
        for (Goal goal : essayGoals) {
            // 3) Find essay executions done during the goal's time period, one per distinct essay
            List<EssayExecution> relevantExecutions = essayExecutionRepository.findByStudent(student).stream()
                    .filter(execution -> execution.getEssay().getClassroom().getId().equals(
                            goal.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toList()).get(0)))
                    .filter(execution -> execution.getCreatedAt().isAfter(goal.getOpeningDate())
                            && execution.getCreatedAt().isBefore(goal.getFinalDate()))
                    // Group by Essay and select the most recent execution
                    .collect(Collectors.groupingBy(
                            execution -> execution.getEssay(),
                            Collectors.maxBy(Comparator.comparing(EssayExecution::getCreatedAt))
                    ))
                    .values()
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            // 4) Check if a goal execution already exists
            GoalExecution goalExecution = goalExecutionRepository.findByGoalAndStudent(goal, student)
                    .orElse(null);

            if (goalExecution == null) {
                // 5) Create new goal execution if it doesn't exist
                goalExecution = new GoalExecution();
                goalExecution.setGoal(goal);
                goalExecution.setStudent(student);
                // Set amount to the number of unique essay executions
                goalExecution.setAmount(relevantExecutions.size());
                
                goalExecutionRepository.save(goalExecution);
            } else {
                // 6) Update existing goal execution
                goalExecution.setAmount(goalExecution.getAmount() + 1);
                
                goalExecutionRepository.save(goalExecution);
            }
        }

        return essayExecutionMapper.toResponseDTO(savedExecution);
    }
    
    public EssayExecutionResponseDTO update(String id, EssayExecutionCreateDTO updateDTO) {
        EssayExecution essayExecution = essayExecutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Essay execution not found with id: " + id));
        
        essayExecutionMapper.updateEntityFromDTO(essayExecution, updateDTO);
        EssayExecution updatedExecution = essayExecutionRepository.save(essayExecution);
        return essayExecutionMapper.toResponseDTO(updatedExecution);
    }

    public void deleteById(String id) {
        essayExecutionRepository.deleteById(id);
    }
}