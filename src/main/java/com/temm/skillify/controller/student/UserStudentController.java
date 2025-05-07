package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.MessageCreateDTO;
import com.temm.skillify.model.dto.response.LevelProgressResponseDTO;
import com.temm.skillify.model.dto.response.MessageResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.service.MessageService;
import com.temm.skillify.service.UserService;
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
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
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
}