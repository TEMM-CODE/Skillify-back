package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.request.CourseCreateDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/mentor/courses")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseMentorController {

    @Autowired
    private CourseService courseMentorService;


    @GetMapping
    public ResponseEntity<Set<CourseResponseDTO>> getAllCoursesByMentor() {
        return ResponseEntity.ok(courseMentorService.getAllCoursesByCurrentMentor());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseMentorService.getCourseById(id));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
        return new ResponseEntity<>(courseMentorService.createCourse(courseCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable String id, @RequestBody CourseCreateDTO courseCreateDTO) {
        return ResponseEntity.ok(courseMentorService.updateCourse(id, courseCreateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseMentorService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}