package com.temm.skillify.service;


import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.EssayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Essay> getEssaysByClassroom(String classroomId) {
        User currentUser = getCurrentUser();
        
        // Verify the mentor has access to this classroom
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new NoSuchElementException("Classroom not found with id: " + classroomId));
        
        if (!classroom.getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this classroom");
        }
        
        return essayRepository.findByClassroom(classroom);
    }

    public Essay getEssayById(String id) {
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Essay not found with id: " + id));
        
        // Verify the mentor has access to the classroom of this essay
        User currentUser = getCurrentUser();
        if (!essay.getClassroom().getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this essay");
        }
        
        return essay;
    }

    public Essay createEssay(Essay essay) {
        // Verify mentor has access to the classroom
        User currentUser = getCurrentUser();
        Classroom classroom = classroomRepository.findById(essay.getClassroom().getId())
                .orElseThrow(() -> new NoSuchElementException("Classroom not found"));
        
        if (!classroom.getMentor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to create essays for this classroom");
        }
        
        return essayRepository.save(essay);
    }

    public Essay updateEssay(String id, Essay updatedEssay) {
        Essay existingEssay = getEssayById(id);
        
        // Update fields
        existingEssay.setTheme(updatedEssay.getTheme());
        existingEssay.setDescription(updatedEssay.getDescription());
        existingEssay.setMinWords(updatedEssay.getMinWords());
        existingEssay.setMaxDate(updatedEssay.getMaxDate());
        
        // Cannot change the classroom of an essay
        
        return essayRepository.save(existingEssay);
    }

    public void deleteEssay(String id) {
        Essay essay = getEssayById(id);
        essayRepository.delete(essay);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}