package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.CourseLessonCreateDTO;
import com.temm.skillify.model.dto.response.CourseLessonResponseDTO;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.mapper.CourseLessonMapper;
import com.temm.skillify.service.CourseLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentor/course-lessons")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseLessonMentorController {

    @Autowired
    private CourseLessonService courseLessonService;
    
    @Autowired
    private CourseLessonMapper courseLessonMapper;
    
    @GetMapping
    public ResponseEntity<List<CourseLessonResponseDTO>> getAllLessonsByMentor() {
        List<CourseLesson> lessons = courseLessonService.getAllLessonsByMentor();
        List<CourseLessonResponseDTO> responseDTOs = lessons.stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonResponseDTO>> getLessonsByCourse(@PathVariable String courseId) {
        List<CourseLesson> lessons = courseLessonService.getLessonsByCourse(courseId);
        List<CourseLessonResponseDTO> responseDTOs = lessons.stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseLessonResponseDTO>> getLessonsByCategory(@PathVariable String categoryId) {
        List<CourseLesson> lessons = courseLessonService.getLessonsByCategory(categoryId);
        List<CourseLessonResponseDTO> responseDTOs = lessons.stream()
                .map(courseLessonMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseLessonResponseDTO> getLessonById(@PathVariable String id) {
        CourseLesson lesson = courseLessonService.getLessonById(id);
        return ResponseEntity.ok(courseLessonMapper.toResponseDTO(lesson));
    }
    
    @PostMapping
    public ResponseEntity<CourseLessonResponseDTO> createLesson(@RequestBody CourseLessonCreateDTO createDTO) {
        CourseLesson createdLesson = courseLessonService.createLesson(createDTO);
        return new ResponseEntity<>(courseLessonMapper.toResponseDTO(createdLesson), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseLessonResponseDTO> updateLesson(
            @PathVariable String id, 
            @RequestBody CourseLessonCreateDTO updateDTO) {
        CourseLesson updatedLesson = courseLessonService.updateLesson(id, updateDTO);
        return ResponseEntity.ok(courseLessonMapper.toResponseDTO(updatedLesson));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable String id) {
        courseLessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}