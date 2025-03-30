package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.dto.request.MessageCreateDTO;
import com.temm.skillify.model.dto.response.MessageResponseDTO;
import com.temm.skillify.model.entity.Message;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.MessageMapper;
import com.temm.skillify.repository.MessageRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ClassroomService classroomService;
    private final UserService userService;
    private final MessageMapper messageMapper;
    
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
    
    public List<MessageResponseDTO> findAllDTO() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MessageResponseDTO> findByRemetenteDTO(User remetente) {
        return messageRepository.findByRemetente(remetente).stream()
                .map(messageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MessageResponseDTO> findByDestinatarioDTO(User destinatario) {
        return messageRepository.findByDestinatario(destinatario).stream()
                .map(messageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<Message> findByRemetente(User remetente) {
        return messageRepository.findByRemetente(remetente);
    }
    
    public List<Message> findByDestinatario(User destinatario) {
        return messageRepository.findByDestinatario(destinatario);
    }
    
    public Optional<Message> findById(String id) {
        return messageRepository.findById(id);
    }
    
    public Optional<MessageResponseDTO> findByIdDTO(String id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponseDTO);
    }
    
    public Message save(Message message) {
        return messageRepository.save(message);
    }
    
    public MessageResponseDTO createMessageFromDTO(MessageCreateDTO dto, User sender) {
        User recipient = userService.findById(dto.getDestinatarioId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        
        Message message = new Message();
        message.setContent(dto.getContent());
        message.setRemetente(sender);
        message.setDestinatario(recipient);
        
        Message savedMessage = messageRepository.save(message);
        return messageMapper.toResponseDTO(savedMessage);
    }
    
    public void deleteById(String id) {
        messageRepository.deleteById(id);
    }
    
    // Helper method to check if a user is a mentor in any of student's classrooms
    public boolean isMentorOfStudentClassroom(User student, User potentialMentor) {
        return classroomService.findAll().stream()
                .filter(classroom -> classroom.getStudents().contains(student))
                .anyMatch(classroom -> classroom.getMentor().equals(potentialMentor));
    }

    public boolean isStudentOfMentorClassroom(User student, User potentialMentor) {
        return classroomService.findAll().stream()
                .filter(classroom -> classroom.getMentor().equals(potentialMentor))
                .anyMatch(classroom -> classroom.getStudents().contains(student));
    }
}