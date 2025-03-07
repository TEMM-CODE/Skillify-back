package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.service.CourseLessonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/course-lessons")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class CourseLessonMentorController {

    @Autowired
    private CourseLessonService courseLessonMentorService;

    @GetMapping
    public ResponseEntity<List<CourseLesson>> getAllLessonsByMentor() {
        return ResponseEntity.ok(courseLessonMentorService.getAllLessonsByMentor());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLesson>> getLessonsByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(courseLessonMentorService.getLessonsByCourse(courseId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseLesson>> getLessonsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(courseLessonMentorService.getLessonsByCategory(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseLesson> getLessonById(@PathVariable String id) {
        return ResponseEntity.ok(courseLessonMentorService.getLessonById(id));
    }

    @PostMapping
    public ResponseEntity<CourseLesson> createLesson(@RequestBody CourseLesson lesson) {
        return new ResponseEntity<>(courseLessonMentorService.createLesson(lesson), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseLesson> updateLesson(@PathVariable String id, @RequestBody CourseLesson lesson) {
        return ResponseEntity.ok(courseLessonMentorService.updateLesson(id, lesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable String id) {
        courseLessonMentorService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}