package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;
import com.temm.skillify.repository.ClassroomAccessTokenRepository;
import com.temm.skillify.model.dto.response.ClassroomAccessTokenResponseDTO;
import com.temm.skillify.model.dto.response.ClassroomResponseDTO;
import com.temm.skillify.model.dto.request.ClassroomAccessTokenCreateDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomAccessTokenService {
    private final ClassroomAccessTokenRepository tokenRepository;
    private final ClassroomService classroomService;

    public List<ClassroomAccessTokenResponseDTO> findAll() {
        return tokenRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClassroomAccessTokenResponseDTO> findByClassroomId(String classroomId) {
        Classroom classroom = classroomService.findById(classroomId).orElseThrow();
        return tokenRepository.findByClassroom(classroom).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClassroomAccessTokenResponseDTO> findById(String id) {
        return tokenRepository.findById(id).map(this::convertToResponseDTO);
    }

    public Optional<ClassroomAccessTokenResponseDTO> findByToken(String token) {
        return tokenRepository.findByToken(token).map(this::convertToResponseDTO);
    }

    public ClassroomAccessTokenResponseDTO save(ClassroomAccessTokenCreateDTO createDTO) {
        Classroom classroom = classroomService.findById(createDTO.getClassroomId()).orElseThrow();
        
        ClassroomAccessToken token = new ClassroomAccessToken();
        token.setClassroom(classroom);
        token.setToken(createDTO.getToken() != null ? createDTO.getToken() : UUID.randomUUID().toString());
        
        return convertToResponseDTO(tokenRepository.save(token));
    }

    public ClassroomAccessTokenResponseDTO generateToken(String classroomId) {
        Classroom classroom = classroomService.findById(classroomId).orElseThrow();
        ClassroomAccessToken token = new ClassroomAccessToken();
        token.setClassroom(classroom);
        token.setToken(UUID.randomUUID().toString());
        return convertToResponseDTO(tokenRepository.save(token));
    }

    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(t -> tokenRepository.deleteById(t.getId()));
    }

    private ClassroomAccessTokenResponseDTO convertToResponseDTO(ClassroomAccessToken token) {
        ClassroomAccessTokenResponseDTO responseDTO = new ClassroomAccessTokenResponseDTO();
        responseDTO.setId(token.getId());
        responseDTO.setCreatedAt(token.getCreatedAt());
        responseDTO.setUpdatedAt(token.getUpdatedAt());
        responseDTO.setToken(token.getToken());
        
        // Assuming there's a method to convert Classroom to ClassroomResponseDTO
        ClassroomResponseDTO classroomDTO = new ClassroomResponseDTO();
        classroomDTO.setId(token.getClassroom().getId());
        // Set other classroom properties as needed
        
        responseDTO.setClassroom(classroomDTO);
        return responseDTO;
    }
}