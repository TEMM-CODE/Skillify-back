package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.ClassroomAccessTokenService;
import com.temm.skillify.service.ClassroomService;
import com.temm.skillify.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/classrooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class ClassroomStudentController {
    private final ClassroomService classroomService;
    private final ClassroomAccessTokenService tokenService;
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getClassroomById(@PathVariable String id, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        
        return classroomService.findDTOById(id)
            .filter(dto -> {
                // We need to check if the student is part of this classroom
                return classroomService.findById(id)
                    .map(classroom -> classroom.getStudents().contains(student))
                    .orElse(false);
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/join/{token}")
    public ResponseEntity<ClassroomResponseDTO> joinClassroom(@PathVariable String token, Authentication authentication) {
        return tokenService.findByToken(token)
            .map(accessTokenDTO -> {
                // Get the classroom ID from the token DTO
                String classroomId = accessTokenDTO.getClassroom().getId();
                
                // Find the actual classroom entity
                return classroomService.findById(classroomId)
                    .map(classroom -> {
                        // Add student to the classroom
                        User student = userService.findByEmail(authentication.getName()).orElseThrow();
                        classroom.getStudents().add(student);
                        
                        // Save the updated classroom
                        Classroom updatedClassroom = classroomService.save(classroom);
                        
                        // Convert the entity to DTO
                        return classroomService.findDTOById(updatedClassroom.getId())
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
                    })
                    .orElse(ResponseEntity.notFound().build());
            })
            .orElse(ResponseEntity.notFound().build());
    }
}