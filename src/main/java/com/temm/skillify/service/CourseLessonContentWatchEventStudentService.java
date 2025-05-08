package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseLessonContentWatchEventRequestDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentWatchEventResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.GoalType;
import com.temm.skillify.model.mapper.CourseLessonContentWatchEventMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.CourseLessonContentWatchEventRepository;
import com.temm.skillify.repository.ExperienceEventRepository;
import com.temm.skillify.repository.GoalExecutionRepository;
import com.temm.skillify.repository.GoalRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonContentWatchEventStudentService {

    private final CourseLessonContentWatchEventRepository watchEventRepository;
    private final UserService userService;
    private final CourseLessonContentWatchEventMapper watchEventMapper;
    private final GoalRepository goalRepository;
    private final GoalExecutionRepository goalExecutionRepository;
    private final ClassroomRepository classroomRepository;
    private final GamificationService gamificationService;
    private final ExperienceEventRepository experienceEventRepository;

    public List<CourseLessonContentWatchEventResponseDTO> getAllWatchEventsForStudent(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        List<CourseLessonContentWatchEvent> watchEvents = watchEventRepository.findByStudent(student);

        return watchEvents.stream()
                .map(watchEventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseLessonContentWatchEventResponseDTO getWatchEventById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        CourseLessonContentWatchEvent watchEvent = watchEventRepository.findByIdAndStudent(id, student)
                .orElseThrow(() -> new EntityNotFoundException("Watch event not found with id: " + id));

        return watchEventMapper.toResponseDTO(watchEvent);
    }

   @Transactional
    public CourseLessonContentWatchEventResponseDTO createWatchEvent(CourseLessonContentWatchEventRequestDTO requestDTO, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        // Ensure the student ID in the request matches the authenticated user
        if (!requestDTO.getStudentId().equals(student.getId())) {
            throw new IllegalArgumentException("Student ID in request does not match authenticated user");
        }

        CourseLessonContentWatchEvent watchEvent = watchEventMapper.toEntity(requestDTO);
        CourseLessonContentWatchEvent savedWatchEvent = watchEventRepository.save(watchEvent);

        // Award XP for lesson watch and create ExperienceEvent
        int xpAwarded = GamificationService.EntityType.LESSON.getXp();
        gamificationService.awardXpForEntity(student, GamificationService.EntityType.LESSON);

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

        // 2) Filter by type LESSON
        List<Goal> lessonGoals = activeGoals.stream()
                .filter(goal -> goal.getType() == GoalType.LESSON)
                .collect(Collectors.toList());

        // Process each lesson goal
        for (Goal goal : lessonGoals) {
            // Get the classrooms associated with the goal
            List<String> goalClassroomIds = goal.getClassrooms().stream()
                    .map(Classroom::getId)
                    .collect(Collectors.toList());

            // 3) Find watch events done during the goal's time period, one per distinct lesson content
            List<CourseLessonContentWatchEvent> relevantWatchEvents = watchEventRepository.findByStudent(student).stream()
                    .filter(event -> {
                        Course course = event.getCourseLessonContent().getCourseLesson().getCourse();
                        return goal.getClassrooms().stream()
                                .anyMatch(classroom -> classroom.getCourses().contains(course));
                    })
                    .filter(event -> event.getCreatedAt().isAfter(goal.getOpeningDate())
                            && event.getCreatedAt().isBefore(goal.getFinalDate()))
                    // Group by CourseLessonContent and select the most recent watch event
                    .collect(Collectors.groupingBy(
                            event -> event.getCourseLessonContent(),
                            Collectors.maxBy(Comparator.comparing(CourseLessonContentWatchEvent::getCreatedAt))
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
                // Set amount to the number of unique lesson watch events
                goalExecution.setAmount(relevantWatchEvents.size());
                goalExecutionRepository.save(goalExecution);
            } else {
                // 6) Update existing goal execution
                goalExecution.setAmount(goalExecution.getAmount() + 1);
                goalExecutionRepository.save(goalExecution);
            }
        }

        return watchEventMapper.toResponseDTO(savedWatchEvent);
    }
}