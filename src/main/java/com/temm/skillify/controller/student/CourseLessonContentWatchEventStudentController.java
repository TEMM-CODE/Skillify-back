package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.CourseLessonContentWatchEventRequestDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentWatchEventResponseDTO;
import com.temm.skillify.service.CourseLessonContentWatchEventStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/course-lesson-content-watch-events")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class CourseLessonContentWatchEventStudentController {

    private final CourseLessonContentWatchEventStudentService watchEventService;

    @GetMapping
    public ResponseEntity<List<CourseLessonContentWatchEventResponseDTO>> getAllWatchEvents(Authentication authentication) {
        return ResponseEntity.ok(watchEventService.getAllWatchEventsForStudent(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonContentWatchEventResponseDTO> getWatchEventById(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(watchEventService.getWatchEventById(id, authentication));
    }

    @PostMapping
    public ResponseEntity<CourseLessonContentWatchEventResponseDTO> createWatchEvent(
            @RequestBody CourseLessonContentWatchEventRequestDTO requestDTO,
            Authentication authentication) {
        return ResponseEntity.ok(watchEventService.createWatchEvent(requestDTO, authentication));
    }
}
