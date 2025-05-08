package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.response.MentorProgressStudent;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.service.UserMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentor/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class UserMentorController {
    
    private final UserMentorService userMentorService;
    private final UserMapper userMapper;
    
    @GetMapping
    public ResponseEntity<Set<UserResponseDTO>> getAllStudents(Authentication authentication) {
        // Get the authenticated mentor user from the security context
        User mentor = (User) authentication.getPrincipal();
        
        // Get all unique students for this mentor
        Set<User> students = userMentorService.getAllStudentsForMentor(mentor);
        
        // Convert to DTOs
        Set<UserResponseDTO> studentDtos = students.stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toSet());
        
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<MentorProgressStudent>> getStudentsForClassroom(
            @PathVariable String classroomId, Authentication authentication) {
        // Get the authenticated mentor user from the security context
        User mentor = (User) authentication.getPrincipal();
        
        // Get students for the specified classroom
        List<MentorProgressStudent> students = userMentorService.getStudentsForClassroom(classroomId, mentor);
        
        return ResponseEntity.ok(students);
    }
}