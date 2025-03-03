package com.temm.skillify.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Message;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ClassroomService classroomService;

    public List<Message> findAll() {
        return messageRepository.findAll();
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

    public Message save(Message message) {
        return messageRepository.save(message);
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
}