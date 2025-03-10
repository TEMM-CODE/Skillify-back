package com.temm.skillify.service;


import com.temm.skillify.model.dto.request.EssayCreateDTO;
import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.EssayMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.EssayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private EssayMapper essayMapper;

    public List<EssayResponseDTO> getEssaysByClassroom(String classroomId) {
        User currentUser = getCurrentUser();
        // Verify the mentor has access to this classroom
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new NoSuchElementException("Classroom not found with id: " + classroomId));
        
        if (!classroom.getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this classroom");
        }
        
        List<Essay> essays = essayRepository.findByClassroom(classroom);
        return essays.stream()
                .map(essayMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EssayResponseDTO getEssayById(String id) {
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Essay not found with id: " + id));
        
        // Verify the mentor has access to the classroom of this essay
        User currentUser = getCurrentUser();
        if (!essay.getClassroom().getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this essay");
        }
        
        return essayMapper.toResponseDTO(essay);
    }

    public EssayResponseDTO createEssay(EssayCreateDTO essayCreateDTO) {
        // Verify mentor has access to the classroom
        User currentUser = getCurrentUser();
        Classroom classroom = classroomRepository.findById(essayCreateDTO.getClassroomId())
                .orElseThrow(() -> new NoSuchElementException("Classroom not found"));
        
        if (!classroom.getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create essays for this classroom");
        }
        
        Essay essay = essayMapper.toEntity(essayCreateDTO, classroom);
        Essay savedEssay = essayRepository.save(essay);
        
        return essayMapper.toResponseDTO(savedEssay);
    }

    public EssayResponseDTO updateEssay(String id, EssayCreateDTO essayCreateDTO) {
        Essay existingEssay = getExistingEssay(id);
        
        // Update fields
        essayMapper.updateEntityFromDTO(existingEssay, essayCreateDTO);
        
        Essay updatedEssay = essayRepository.save(existingEssay);
        return essayMapper.toResponseDTO(updatedEssay);
    }

    public void deleteEssay(String id) {
        Essay essay = getExistingEssay(id);
        essayRepository.delete(essay);
    }

    private Essay getExistingEssay(String id) {
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Essay not found with id: " + id));
        
        // Verify the mentor has access to the classroom of this essay
        User currentUser = getCurrentUser();
        if (!essay.getClassroom().getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this essay");
        }
        
        return essay;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}