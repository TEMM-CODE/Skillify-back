package com.temm.skillify.service;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserMentorService {
    
    private final ClassroomRepository classroomRepository;
    
    public Set<User> getAllStudentsForMentor(User mentor) {
        // Get all classrooms where the user is a mentor
        List<Classroom> classrooms = classroomRepository.findByMentor(mentor);
        
        // Use a Set to ensure unique students across all classrooms
        Set<User> uniqueStudents = new HashSet<>();
        
        // Iterate through all classrooms and add their students to the set
        for (Classroom classroom : classrooms) {
            uniqueStudents.addAll(classroom.getStudents());
        }
        
        return uniqueStudents;
    }
}