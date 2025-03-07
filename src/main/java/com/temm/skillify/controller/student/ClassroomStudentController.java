package com.temm.skillify.controller.student;

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
    public ResponseEntity<Classroom> getClassroomById(@PathVariable String id, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        
        return classroomService.findById(id)
                .filter(classroom -> classroom.getStudents().contains(student))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/join/{token}")
    public ResponseEntity<Classroom> joinClassroom(@PathVariable String token, Authentication authentication) {
        return tokenService.findByToken(token)
                .map(accessToken -> {
                    Classroom classroom = accessToken.getClassroom();
                    User student = userService.findByEmail(authentication.getName()).orElseThrow();
                    
                    classroom.getStudents().add(student);
                    Classroom updatedClassroom = classroomService.save(classroom);
                    
                    return ResponseEntity.ok(updatedClassroom);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}