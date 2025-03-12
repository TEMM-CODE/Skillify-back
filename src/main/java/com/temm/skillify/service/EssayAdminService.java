package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.dto.request.EssayCreateDTO;
import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.mapper.EssayMapper;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.repository.ClassroomRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayAdminService {
    private final EssayRepository essayRepository;
    private final ClassroomRepository classroomRepository;
    private final EssayMapper essayMapper;

    public List<EssayResponseDTO> findAll() {
        return essayRepository.findAll().stream()
                .map(essayMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EssayResponseDTO> findById(String id) {
        return essayRepository.findById(id)
                .map(essayMapper::toResponseDTO);
    }

    public List<EssayResponseDTO> findByClassroom(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        return essayRepository.findByClassroom(classroom).stream()
                .map(essayMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EssayResponseDTO create(EssayCreateDTO essayCreateDTO) {
        Classroom classroom = classroomRepository.findById(essayCreateDTO.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
                
        Essay essay = essayMapper.toEntity(essayCreateDTO, classroom);
        Essay savedEssay = essayRepository.save(essay);
        return essayMapper.toResponseDTO(savedEssay);
    }

    public EssayResponseDTO update(String id, EssayCreateDTO essayCreateDTO) {
        Essay existingEssay = essayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Essay not found"));

        // Update classroom if classroomId is provided
        if (essayCreateDTO.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(essayCreateDTO.getClassroomId())
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            existingEssay.setClassroom(classroom);
        }

        // Update essay properties
        essayMapper.updateEntityFromDTO(existingEssay, essayCreateDTO);
        
        Essay updatedEssay = essayRepository.save(existingEssay);
        return essayMapper.toResponseDTO(updatedEssay);
    }

    public void deleteById(String id) {
        essayRepository.deleteById(id);
    }
}