package com.temm.skillify.controller.mentor;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.service.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/classrooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MENTOR')")
public class ClassroomMentorController {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms(Authentication authentication) {
        return ResponseEntity.ok(classroomService.findAllByMentorEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable String id) {
        return classroomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom, Authentication authentication) {
        // Ensure the mentor is the current authenticated user
        return ResponseEntity.ok(classroomService.saveForMentor(classroom, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable String id, @RequestBody Classroom classroom, Authentication authentication) {
        return classroomService.findByIdAndMentorEmail(id, authentication.getName())
                .map(existingClassroom -> {
                    classroom.setId(id);
                    return ResponseEntity.ok(classroomService.save(classroom));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String id, Authentication authentication) {
        return classroomService.findByIdAndMentorEmail(id, authentication.getName())
                .map(classroom -> {
                    classroomService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}