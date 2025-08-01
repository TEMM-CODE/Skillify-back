package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.CourseLessonContentCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentResponseDTO;
import com.temm.skillify.service.CourseLessonContentMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/course-lesson-content")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseLessonContentMentorController {

    @Autowired
    private CourseLessonContentMentorService contentService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseLessonContentResponseDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllCourseLessonContents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonContentResponseDTO> getContentById(@PathVariable String id) {
        return ResponseEntity.ok(contentService.getCourseLessonContentById(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CourseLessonContentResponseDTO> createContent(
            @ModelAttribute CourseLessonContentCreateDTO contentDTO) {
        return new ResponseEntity<>(contentService.createContent(contentDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<CourseLessonContentResponseDTO> updateContent(
            @PathVariable String id,
            @ModelAttribute CourseLessonContentCreateDTO contentDTO) {
        return ResponseEntity.ok(contentService.updateContent(id, contentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable String id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}