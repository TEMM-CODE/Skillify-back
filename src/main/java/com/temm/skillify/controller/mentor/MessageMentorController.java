package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.MessageCreateDTO;
import com.temm.skillify.model.dto.response.MessageResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.service.MessageService;
import com.temm.skillify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class MessageMentorController {
    private final MessageService messageService;
    private final UserService userService;
    
    @GetMapping("/sent")
    public ResponseEntity<List<MessageResponseDTO>> getSentMessages(Authentication authentication) {
        User mentor = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByRemetenteDTO(mentor));
    }
    
    @GetMapping("/received")
    public ResponseEntity<List<MessageResponseDTO>> getReceivedMessages(Authentication authentication) {
        User mentor = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByDestinatarioDTO(mentor));
    }

    @GetMapping("/received/{studentId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesWithStudent(Authentication authentication, @PathVariable("studentId") String studentId){
        User mentor = userService.findByEmail(authentication.getName()).orElseThrow();
        User student = userService.findById(studentId).orElseThrow();
        boolean isStudentOfMentorClassroom = messageService.isStudentOfMentorClassroom(student, mentor);
            if (!isStudentOfMentorClassroom) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(messageService.findByDestinatarioDTO(student));
    }
    
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageCreateDTO messageDTO, Authentication authentication) {
        User mentor = userService.findByEmail(authentication.getName()).orElseThrow();
        User recipient = userService.findById(messageDTO.getDestinatarioId()).orElseThrow();
        
        if(!recipient.getRole().equals(UserRole.ADMIN)){
            boolean isStudentOfMentorClassroom = messageService.isStudentOfMentorClassroom(recipient, mentor);
            if (!isStudentOfMentorClassroom) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        MessageResponseDTO response = messageService.createMessageFromDTO(messageDTO, mentor);
        return ResponseEntity.ok(response);
    }
}
