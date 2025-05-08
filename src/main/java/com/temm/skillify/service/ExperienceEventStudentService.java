package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.ExperienceEventResponseDTO;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.repository.ExperienceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceEventStudentService {

    private final ExperienceEventRepository experienceEventRepository;
    private final UserService userService;

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

    // Validate if the current user is the owner of the experience event
    private void validateStudentOwnership(User currentStudent, ExperienceEvent event) {
        if (!Objects.equals(currentStudent.getId(), event.getUser().getId())) {
            throw new SecurityException("You can only access your own experience events");
        }
    }

    @Transactional(readOnly = true)
    public List<ExperienceEventResponseDTO> getAll() {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<ExperienceEvent> events = experienceEventRepository.findByUser(currentStudent).stream()
                .filter(event -> event.getCreatedAt().isAfter(threeMonthsAgo))
                .sorted(Comparator.comparing(ExperienceEvent::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return events.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExperienceEventResponseDTO getById(String id) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        ExperienceEvent event = experienceEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience event not found with id: " + id));

        validateStudentOwnership(currentStudent, event);
        return toResponseDTO(event);
    }

    @Transactional(readOnly = true)
    public int getSequence() {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        List<ExperienceEvent> events = experienceEventRepository.findByUserOrderByCreatedAtDesc(currentStudent);
        if (events.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate expectedDate = today;

        // Iterate through events to find consecutive days
        for (ExperienceEvent event : events) {
            LocalDate eventDate = event.getCreatedAt().toLocalDate();
            if (eventDate.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (eventDate.isBefore(expectedDate)) {
                break; // Break if there's a gap in the streak
            }
        }

        return streak;
    }

    @Transactional(readOnly = true)
    public int getExperiencePerMonth(int year, int month) {
        User currentStudent = getCurrentUser();
        validateStudentRole(currentStudent);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<ExperienceEvent> events = experienceEventRepository.findByUser(currentStudent).stream()
                .filter(event -> !event.getCreatedAt().isBefore(startOfMonth) && !event.getCreatedAt().isAfter(endOfMonth))
                .collect(Collectors.toList());

        return events.stream().mapToInt(ExperienceEvent::getXp).sum();
    }

    private ExperienceEventResponseDTO toResponseDTO(ExperienceEvent event) {
        ExperienceEventResponseDTO dto = new ExperienceEventResponseDTO();
        dto.id = event.getId();
        dto.userId = event.getUser().getId();
        dto.xp = event.getXp();
        dto.createdAt = event.getCreatedAt();
        return dto;
    }
}