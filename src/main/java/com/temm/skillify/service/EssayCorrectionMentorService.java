package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.EssayCorrectionCreateDTO;
import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.EssayCorrectionMapper;
import com.temm.skillify.repository.EssayCorrectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EssayCorrectionMentorService {

    private final EssayCorrectionRepository essayCorrectionRepository;
    private final EssayCorrectionMapper essayCorrectionMapper;
    private final UserService userService;

    // Get current authenticated mentor
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // Validate if current user is a mentor
    private void validateMentorRole(User user) {
        if (!UserRole.MENTOR.equals(user.getRole())) {
            throw new SecurityException("Only mentors can perform this action");
        }
    }

    // Validate if the current user is the assigned mentor
    private void validateMentorOwnership(User currentMentor, EssayCorrection correction) {
        if (!Objects.equals(currentMentor.getId(), correction.getMentor().getId())) {
            throw new SecurityException("You can only access your own corrections");
        }
    }

    @Transactional(readOnly = true)
    public List<EssayCorrectionResponseDTO> getAllMentorCorrections() {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        List<EssayCorrection> corrections = essayCorrectionRepository.findByMentor(currentMentor);
        return essayCorrectionMapper.toResponseDTOList(corrections);
    }

    @Transactional(readOnly = true)
    public EssayCorrectionResponseDTO getCorrectionById(String id) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        EssayCorrection correction = essayCorrectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correction not found with id: " + id));
        
        validateMentorOwnership(currentMentor, correction);
        return essayCorrectionMapper.toResponseDTO(correction);
    }

    @Transactional
    public EssayCorrectionResponseDTO createCorrection(EssayCorrectionCreateDTO createDTO) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        // Ensure the mentorId in DTO matches current user
        if (!Objects.equals(currentMentor.getId(), createDTO.getMentorId())) {
            throw new SecurityException("You can only create corrections for yourself as mentor");
        }

        EssayCorrection correction = essayCorrectionMapper.toEntity(createDTO);
        EssayCorrection savedCorrection = essayCorrectionRepository.save(correction);
        return essayCorrectionMapper.toResponseDTO(savedCorrection);
    }

    @Transactional
    public EssayCorrectionResponseDTO updateCorrection(String id, EssayCorrectionCreateDTO updateDTO) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        EssayCorrection existingCorrection = essayCorrectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correction not found with id: " + id));
        
        validateMentorOwnership(currentMentor, existingCorrection);

        essayCorrectionMapper.updateEntityFromDTO(existingCorrection, updateDTO);
        EssayCorrection updatedCorrection = essayCorrectionRepository.save(existingCorrection);
        return essayCorrectionMapper.toResponseDTO(updatedCorrection);
    }

    @Transactional
    public void deleteCorrection(String id) {
        User currentMentor = getCurrentUser();
        validateMentorRole(currentMentor);

        EssayCorrection correction = essayCorrectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correction not found with id: " + id));
        
        validateMentorOwnership(currentMentor, correction);
        essayCorrectionRepository.delete(correction);
    }
}