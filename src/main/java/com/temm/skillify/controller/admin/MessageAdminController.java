package com.temm.skillify.controller.admin;


import com.temm.skillify.model.dto.request.MessageCreateDTO;
import com.temm.skillify.model.dto.response.MessageResponseDTO;
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
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MessageAdminController {
    private final MessageService messageService;
    private final UserService userService;
    
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
        
        
        MessageResponseDTO response = messageService.createMessageFromDTO(messageDTO, student);
        return ResponseEntity.ok(response);
    }    
}
