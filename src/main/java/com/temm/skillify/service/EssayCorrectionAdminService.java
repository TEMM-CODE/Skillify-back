package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.request.EssayCorrectionCreateDTO;
import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.EssayCorrectionMapper;
import com.temm.skillify.repository.EssayCorrectionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayCorrectionAdminService {
    private final EssayCorrectionRepository essayCorrectionRepository;
    private final EssayCorrectionMapper essayCorrectionMapper;

    public List<EssayCorrectionResponseDTO> findAll() {
        List<EssayCorrection> corrections = essayCorrectionRepository.findAll();
        return essayCorrectionMapper.toResponseDTOList(corrections);
    }

    public Optional<EssayCorrectionResponseDTO> findById(String id) {
        return essayCorrectionRepository.findById(id)
                .map(essayCorrectionMapper::toResponseDTO);
    }

    public List<EssayCorrectionResponseDTO> findByMentor(User mentor) {
        List<EssayCorrection> corrections = essayCorrectionRepository.findByMentor(mentor);
        return essayCorrectionMapper.toResponseDTOList(corrections);
    }

    public List<EssayCorrectionResponseDTO> findByEssay(Essay essay) {
        List<EssayCorrection> corrections = essayCorrectionRepository.findByEssay(essay);
        return essayCorrectionMapper.toResponseDTOList(corrections);
    }

    public Optional<EssayCorrectionResponseDTO> findByEssayExecution(EssayExecution execution) {
        return essayCorrectionRepository.findByEssayExecution(execution)
                .map(essayCorrectionMapper::toResponseDTO);
    }

    public EssayCorrectionResponseDTO save(EssayCorrectionCreateDTO correctionDTO) {
        EssayCorrection entity = essayCorrectionMapper.toEntity(correctionDTO);
        EssayCorrection savedEntity = essayCorrectionRepository.save(entity);
        return essayCorrectionMapper.toResponseDTO(savedEntity);
    }

    public Optional<EssayCorrectionResponseDTO> update(String id, EssayCorrectionCreateDTO updateDTO) {
        return essayCorrectionRepository.findById(id)
                .map(existingCorrection -> {
                    essayCorrectionMapper.updateEntityFromDTO(existingCorrection, updateDTO);
                    EssayCorrection updatedEntity = essayCorrectionRepository.save(existingCorrection);
                    return essayCorrectionMapper.toResponseDTO(updatedEntity);
                });
    }

    public void deleteById(String id) {
        essayCorrectionRepository.deleteById(id);
    }
}