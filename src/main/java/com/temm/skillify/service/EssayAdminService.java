package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.repository.EssayRepository;
import com.temm.skillify.repository.ClassroomRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayAdminService {
    
    private final EssayRepository essayRepository;
    private final ClassroomRepository classroomRepository;
    
    public List<Essay> findAll() {
        return essayRepository.findAll();
    }
    
    public Optional<Essay> findById(String id) {
        return essayRepository.findById(id);
    }
    
    public List<Essay> findByClassroom(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        return essayRepository.findByClassroom(classroom);
    }
    
    public Essay create(Essay essay, String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        essay.setClassroom(classroom);
        return essayRepository.save(essay);
    }
    
    public Essay update(String id, Essay updatedEssay, String classroomId) {
        Essay existingEssay = essayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Essay not found"));
        
        if (classroomId != null) {
            Classroom classroom = classroomRepository.findById(classroomId)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
            existingEssay.setClassroom(classroom);
        }
        
        if (updatedEssay.getTheme() != null) {
            existingEssay.setTheme(updatedEssay.getTheme());
        }
        
        if (updatedEssay.getDescription() != null) {
            existingEssay.setDescription(updatedEssay.getDescription());
        }
        
        if (updatedEssay.getMinWords() != null) {
            existingEssay.setMinWords(updatedEssay.getMinWords());
        }
        
        if (updatedEssay.getMaxDate() != null) {
            existingEssay.setMaxDate(updatedEssay.getMaxDate());
        }
        
        return essayRepository.save(existingEssay);
    }
    
    public void deleteById(String id) {
        essayRepository.deleteById(id);
    }
}