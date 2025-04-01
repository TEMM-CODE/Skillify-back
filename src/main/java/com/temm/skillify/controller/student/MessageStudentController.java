package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.request.MessageCreateDTO;
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
@RequestMapping("/api/student/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class MessageStudentController {
    private final MessageService messageService;
    private final UserService userService;
    private final UserMapper userMapper;
    
    @GetMapping("/sent")
    public ResponseEntity<List<MessageResponseDTO>> getSentMessages(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByRemetenteDTO(student));
    }
    
    @GetMapping("/received")
    public ResponseEntity<List<MessageResponseDTO>> getReceivedMessages(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByDestinatarioDTO(student));
    }
    
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageCreateDTO messageDTO, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        User recipient = userService.findById(messageDTO.getDestinatarioId()).orElseThrow();
        
        // Verify recipient is a mentor of one of the student's classrooms
        boolean isMentorOfStudentClassroom = messageService.isMentorOfStudentClassroom(student, recipient);
        if (!isMentorOfStudentClassroom) {
            return ResponseEntity.badRequest().build();
        }
        
        MessageResponseDTO response = messageService.createMessageFromDTO(messageDTO, student);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mentors")
    public ResponseEntity<Set<UserResponseDTO>> findAvailableTutorsForChat(Authentication authentication){
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        List<User> mentors = messageService.findMentorByStudent(student);
                Set<UserResponseDTO> mentorsReturn = mentors.stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toSet());
        return ResponseEntity.ok(mentorsReturn);
    }
}