package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.ExperienceEventResponseDTO;
import com.temm.skillify.service.ExperienceEventStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/experience-events")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class ExperienceEventStudentController {

    private final ExperienceEventStudentService experienceEventStudentService;

    @GetMapping
    public ResponseEntity<List<ExperienceEventResponseDTO>> getAllExperienceEvents(Authentication authentication) {
        List<ExperienceEventResponseDTO> events = experienceEventStudentService.getAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceEventResponseDTO> getExperienceEventById(
            @PathVariable String id,
            Authentication authentication
    ) {
        ExperienceEventResponseDTO event = experienceEventStudentService.getById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/streak")
    public ResponseEntity<Integer> getExperienceEventStreak(Authentication authentication) {
        int streak = experienceEventStudentService.getSequence();
        return ResponseEntity.ok(streak);
    }

    @GetMapping("/xp-per-month")
    public ResponseEntity<Integer> getExperiencePerMonth(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication
    ) {
        int totalXp = experienceEventStudentService.getExperiencePerMonth(year, month);
        return ResponseEntity.ok(totalXp);
    }
}