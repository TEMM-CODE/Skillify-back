package com.temm.skillify.controller.admin;

import com.temm.skillify.model.dto.request.ClassroomCreateDTO;
import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.entity.ClassroomAccessToken;
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
    public ResponseEntity<List<ClassroomResponseDTO>> getAllClassrooms() {
        return ResponseEntity.ok(classroomAdminService.findAllClassrooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getClassroomById(@PathVariable String id) {
        return classroomAdminService.findClassroomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> createClassroom(@RequestBody ClassroomCreateDTO classroomDTO) {
        return ResponseEntity.ok(classroomAdminService.createClassroom(classroomDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> updateClassroom(
            @PathVariable String id, 
            @RequestBody ClassroomCreateDTO classroomDTO) {
        return ResponseEntity.ok(classroomAdminService.updateClassroom(id, classroomDTO));
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
    public ResponseEntity<ClassroomResponseDTO> updateClassroomMentor(
            @PathVariable String id,
            @RequestBody String mentorId) {
        return ResponseEntity.ok(classroomAdminService.updateClassroomMentor(id, mentorId));
    }

    @PutMapping("/{id}/students")
    public ResponseEntity<ClassroomResponseDTO> updateClassroomStudents(
            @PathVariable String id,
            @RequestBody Set<String> studentIds) {
        return ResponseEntity.ok(classroomAdminService.updateClassroomStudents(id, studentIds));
    }

    @PutMapping("/{id}/courses")
    public ResponseEntity<ClassroomResponseDTO> editCourseClassrooms(
            @PathVariable String id,
            @RequestBody ClassroomCreateDTO classroomDTO) {
        return ResponseEntity.ok(classroomAdminService.editCourseClassrooms(id, classroomDTO));
    }
}