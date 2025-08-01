package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.response.MentorProgressStudent;
import com.temm.skillify.model.dto.response.MonthlyStudentsXpDTO;
import com.temm.skillify.model.dto.response.StudentRankingResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.service.UserMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

     @GetMapping("/classrooms/rankings")
public ResponseEntity<List<StudentRankingResponseDTO>> getStudentRankingsForAllClassrooms(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
) {
    List<StudentRankingResponseDTO> rankings = userMentorService.getStudentRankingsForAllClassrooms(page, size);
    return ResponseEntity.ok(rankings);
}


    @PutMapping("/{studentId}")
    public ResponseEntity<UserResponseDTO> updateStudentProfile(
            @PathVariable String studentId,
            @RequestBody RegisterRequest request,
            Authentication authentication
    ) {
        User mentor = (User) authentication.getPrincipal();
        UserResponseDTO updatedStudent = userMentorService.updateStudentProfileByMentor(studentId, request, mentor);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable String studentId,
            Authentication authentication
    ) {
        User mentor = (User) authentication.getPrincipal();
        userMentorService.deleteStudentByMentor(studentId, mentor);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> createStudent(
            @RequestBody RegisterRequest request,
            Authentication authentication
    ) {
        User mentor = (User) authentication.getPrincipal();
        UserResponseDTO createdStudent = userMentorService.createStudent(request, mentor);
        return ResponseEntity.ok(createdStudent);
    }

        @PutMapping("/{studentId}/classrooms")
    public ResponseEntity<Void> updateStudentClassrooms(
            @PathVariable String studentId,
            @RequestBody List<String> classroomIds,
            Authentication authentication
    ) {
        User mentor = (User) authentication.getPrincipal();
        userMentorService.updateStudentClassrooms(studentId, classroomIds, mentor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/background-color")
public ResponseEntity<String> getBackgroundColor(Authentication authentication) {
    return ResponseEntity.ok(userMentorService.getBackgroundColor());
}

@PostMapping("/background-color")
public ResponseEntity<String> changeBackgroundColor(
        @RequestBody String color, Authentication authentication) {
    return ResponseEntity.ok(userMentorService.changeBackgroundColor(color));
}

  @GetMapping("/xp-monthly")
    public ResponseEntity<List<MonthlyStudentsXpDTO>> getXpCountAllStudentsMonthly() {
        List<MonthlyStudentsXpDTO> xpMonthly = userMentorService.getXpCountAllStudentsMonthly();
        return ResponseEntity.ok(xpMonthly);
    }
}