package com.temm.skillify.controller.student;

import com.temm.skillify.model.entity.Message;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.MessageService;
import com.temm.skillify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ESTUDANTE')")
public class MessageStudentController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/sent")
    public ResponseEntity<List<Message>> getSentMessages(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByRemetente(student));
    }

    @GetMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(messageService.findByDestinatario(student));
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElseThrow();
        message.setRemetente(student);
        
        // Verify recipient is a mentor of one of the student's classrooms
        User destinatario = message.getDestinatario();
        boolean isMentorOfStudentClassroom = messageService.isMentorOfStudentClassroom(student, destinatario);
        
        if (!isMentorOfStudentClassroom) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(messageService.save(message));
    }
}
