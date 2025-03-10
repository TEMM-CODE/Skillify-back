package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TutorSessionMapper {
    
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    
    public TutorSessionResponseDTO toResponseDTO(TutorSession session) {
        if (session == null) {
            return null;
        }
        
        TutorSessionResponseDTO dto = new TutorSessionResponseDTO();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setDate(session.getDate());
        dto.setDateHour(session.getDateHour());
        dto.setType(session.getType());
        dto.setLink(session.getLink());
        dto.setMentor(userMapper.toResponseDTO(session.getMentor()));
        dto.setStudent(userMapper.toResponseDTO(session.getStudent()));
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());
        return dto;
    }
    
    public TutorSession toEntity(TutorSessionCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        TutorSession session = new TutorSession();
        session.setTitle(dto.getTitle());
        session.setDate(dto.getDate());
        session.setDateHour(dto.getDateHour());
        session.setType(dto.getType());
        session.setLink(dto.getLink());
        
        // Set student if studentId is provided
        if (dto.getStudentId() != null && !dto.getStudentId().isEmpty()) {
            User student = userRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            session.setStudent(student);
        }
        
        return session;
    }
}