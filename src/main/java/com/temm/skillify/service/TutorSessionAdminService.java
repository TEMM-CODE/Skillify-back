package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.TutorSessionCreateDTO;
import com.temm.skillify.model.dto.response.TutorSessionResponseDTO;
import com.temm.skillify.model.entity.TutorSession;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.TutorSessionMapper;
import com.temm.skillify.repository.TutorSessionRepository;
import com.temm.skillify.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorSessionAdminService {

    private final TutorSessionRepository tutorSessionRepository;
    private final UserRepository userRepository;
    private final TutorSessionMapper tutorSessionMapper;

    public List<TutorSessionResponseDTO> findAll() {
        return tutorSessionRepository.findAll().stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<TutorSessionResponseDTO> findById(String id) {
        return tutorSessionRepository.findById(id)
                .map(tutorSessionMapper::toResponseDTO);
    }

    public List<TutorSessionResponseDTO> findByMentor(User mentor) {
        return tutorSessionRepository.findByMentor(mentor).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByStudent(User student) {
        return tutorSessionRepository.findByStudent(student).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByMentorAndDate(User mentor, LocalDate date) {
        return tutorSessionRepository.findByMentorAndDate(mentor, date).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TutorSessionResponseDTO> findByStudentAndDate(User student, LocalDate date) {
        return tutorSessionRepository.findByStudentAndDate(student, date).stream()
                .map(tutorSessionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TutorSessionResponseDTO create(TutorSessionCreateDTO createDTO) {
        TutorSession session = tutorSessionMapper.toEntity(createDTO);
        
        // Set mentor based on mentorId
        User mentor = userRepository.findById(createDTO.getMentorId())
                .orElseThrow(() -> new EntityNotFoundException("Mentor not found with ID: " + createDTO.getMentorId()));
        session.setMentor(mentor);
        
        TutorSession savedSession = tutorSessionRepository.save(session);
        return tutorSessionMapper.toResponseDTO(savedSession);
    }

    public TutorSessionResponseDTO update(String id, TutorSessionCreateDTO updateDTO) {
        TutorSession existingSession = tutorSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor session not found with ID: " + id));
        
        // Set mentor based on mentorId
        User mentor = userRepository.findById(updateDTO.getMentorId())
                .orElseThrow(() -> new EntityNotFoundException("Mentor not found with ID: " + updateDTO.getMentorId()));
        existingSession.setMentor(mentor);
        
        // Set student if studentId is provided
        if (updateDTO.getStudentId() != null && !updateDTO.getStudentId().isEmpty()) {
            User student = userRepository.findById(updateDTO.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + updateDTO.getStudentId()));
            existingSession.setStudent(student);
        } else {
            existingSession.setStudent(null);
        }
        
        // Update other fields
        existingSession.setTitle(updateDTO.getTitle());
        existingSession.setDate(updateDTO.getDate());
        existingSession.setDateHour(updateDTO.getDateHour());
        existingSession.setType(updateDTO.getType());
        existingSession.setLink(updateDTO.getLink());
        
        TutorSession updatedSession = tutorSessionRepository.save(existingSession);
        return tutorSessionMapper.toResponseDTO(updatedSession);
    }

    public void deleteById(String id) {
        if (!tutorSessionRepository.existsById(id)) {
            throw new EntityNotFoundException("Tutor session not found with ID: " + id);
        }
        tutorSessionRepository.deleteById(id);
    }
    
    // Required for backward compatibility with the existing code
    public Optional<TutorSession> findEntityById(String id) {
        return tutorSessionRepository.findById(id);
    }
    
    public TutorSession save(TutorSession tutorSession) {
        return tutorSessionRepository.save(tutorSession);
    }
}