package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.request.AvatarUpdateRequest;
import com.temm.skillify.model.dto.request.MessageCreateDTO;
import com.temm.skillify.model.dto.request.PasswordUpdateRequest;
import com.temm.skillify.model.dto.response.LevelProgressResponseDTO;
import com.temm.skillify.model.dto.response.MessageResponseDTO;
import com.temm.skillify.model.dto.response.StudentRankingResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.service.MessageService;
import com.temm.skillify.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ESTUDANTE', 'MENTOR', 'ADMIN')")
public class UserStudentController {

    private final MessageService messageService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/mentors")
    public ResponseEntity<Set<UserResponseDTO>> findMentors(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        List<User> mentors = messageService.findMentorByStudent(student);
        Set<UserResponseDTO> mentorsReturn = mentors.stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toSet());
        return ResponseEntity.ok(mentorsReturn);
    }

    @GetMapping("/level-progress")
    public ResponseEntity<LevelProgressResponseDTO> getLevelProgress(Authentication authentication) {
        LevelProgressResponseDTO progress = userService.getLevelProgress(authentication);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getStudentProfile(Authentication authentication){
        UserResponseDTO student = userService.returnStudentProfile(authentication);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateStudentProfile(
        Authentication authentication,
        @RequestBody RegisterRequest request
    ) {
        UserResponseDTO updatedStudent = userService.updateStudentProfile(authentication, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @PostMapping("/password")
    public ResponseEntity<UserResponseDTO> updateStudentPassword(
        Authentication authentication,
        @Valid @RequestBody PasswordUpdateRequest request
    ) {
        UserResponseDTO updatedStudent = userService.updateStudentPassword(authentication, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @PostMapping("/avatar")
    public ResponseEntity<UserResponseDTO> updateStudentAvatar(
        Authentication authentication,
        @Valid @RequestBody AvatarUpdateRequest request
    ) {
        UserResponseDTO updatedStudent = userService.updateStudentAvatar(authentication, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/classroom/{classroomId}/ranking")
    public ResponseEntity<StudentRankingResponseDTO> getStudentRanking(
            @PathVariable String classroomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        StudentRankingResponseDTO ranking = userService.getStudentRankingByClassroom(classroomId, authentication, page, size);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/classrooms/rankings")
public ResponseEntity<List<StudentRankingResponseDTO>> getStudentRankingsForAllClassrooms(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
) {
    List<StudentRankingResponseDTO> rankings = userService.getStudentRankingsForAllClassrooms(authentication, page, size);
    return ResponseEntity.ok(rankings);
}

@GetMapping("/background-color")
public ResponseEntity<String> getBackgroundColor(Authentication authentication) {
    return ResponseEntity.ok(userService.getBackgroundColor());
}

@PostMapping("/background-color")
public ResponseEntity<String> changeBackgroundColor(
        @RequestBody String color, Authentication authentication) {
    return ResponseEntity.ok(userService.changeBackgroundColor(color));
}
}