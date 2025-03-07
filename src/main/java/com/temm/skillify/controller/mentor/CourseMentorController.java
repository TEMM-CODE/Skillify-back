package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/courses")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseMentorController {

    @Autowired
    private CourseService courseMentorService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCoursesByMentor() {
        return ResponseEntity.ok(courseMentorService.getAllCoursesByCurrentMentor());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseMentorService.getCourseById(id));
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return new ResponseEntity<>(courseMentorService.createCourse(course), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course course) {
        return ResponseEntity.ok(courseMentorService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseMentorService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}