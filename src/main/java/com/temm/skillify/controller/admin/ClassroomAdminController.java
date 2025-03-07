package com.temm.skillify.controller.admin;


import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.ClassroomAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/classrooms")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class ClassroomAdminController {
    
    private final ClassroomAdminService classroomAdminService;
    
    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        return ResponseEntity.ok(classroomAdminService.findAllClassrooms());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable String id) {
        return classroomAdminService.findClassroomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) {
        return ResponseEntity.ok(classroomAdminService.createClassroom(classroom));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable String id, @RequestBody Classroom classroom) {
        return ResponseEntity.ok(classroomAdminService.updateClassroom(id, classroom));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String id) {
        classroomAdminService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/tokens")
    public ResponseEntity<List<ClassroomAccessToken>> getClassroomTokens(@PathVariable String id) {
        return ResponseEntity.ok(classroomAdminService.findTokensByClassroomId(id));
    }
    
    @PostMapping("/{id}/tokens")
    public ResponseEntity<ClassroomAccessToken> createClassroomToken(@PathVariable String id) {
        return ResponseEntity.ok(classroomAdminService.createAccessToken(id));
    }
    
    @DeleteMapping("/tokens/{tokenId}")
    public ResponseEntity<Void> deleteClassroomToken(@PathVariable String tokenId) {
        classroomAdminService.deleteAccessToken(tokenId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/mentor")
    public ResponseEntity<Classroom> updateClassroomMentor(
            @PathVariable String id, 
            @RequestBody User mentor) {
        return ResponseEntity.ok(classroomAdminService.updateClassroomMentor(id, mentor));
    }
    
    @PutMapping("/{id}/students")
    public ResponseEntity<Classroom> updateClassroomStudents(
            @PathVariable String id, 
            @RequestBody Set<User> students) {
        return ResponseEntity.ok(classroomAdminService.updateClassroomStudents(id, students));
    }
}