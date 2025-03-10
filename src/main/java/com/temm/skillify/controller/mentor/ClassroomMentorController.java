package com.temm.skillify.controller.mentor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.ClassroomCreateDTO;
import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.service.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/classrooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class ClassroomMentorController {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> getAllClassrooms(Authentication authentication) {
        return ResponseEntity.ok(classroomService.findAllDTOsByMentorEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getClassroomById(@PathVariable String id) {
        return classroomService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> createClassroom(@RequestBody ClassroomCreateDTO classroomDTO, 
                                                              Authentication authentication) {
        // Ensure the mentor is the current authenticated user
        return ResponseEntity.ok(classroomService.createForMentor(classroomDTO, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> updateClassroom(@PathVariable String id, 
                                                              @RequestBody ClassroomCreateDTO classroomDTO, 
                                                              Authentication authentication) {
        return classroomService.findDTOByIdAndMentorEmail(id, authentication.getName())
                .map(existingClassroom -> {
                    return ResponseEntity.ok(classroomService.update(id, classroomDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String id, Authentication authentication) {
        return classroomService.findDTOByIdAndMentorEmail(id, authentication.getName())
                .map(classroom -> {
                    classroomService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}