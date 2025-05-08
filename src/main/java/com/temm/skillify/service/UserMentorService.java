package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.MentorProgressStudent;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.entity.UserAvatar;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.CourseLessonContentWatchEventRepository;
import com.temm.skillify.repository.ExperienceEventRepository;
import com.temm.skillify.repository.UserAvatarRepository;
import com.temm.skillify.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMentorService {
    
    private final ClassroomRepository classroomRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final UserMapper userMapper;
    private final CourseLessonContentWatchEventRepository courseLessonContentWatchEventRepository;
    private final ExperienceEventRepository experienceEventRepository;
    private final UserRepository userRepository;
    
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
    
    public List<MentorProgressStudent> getStudentsForClassroom(String classroomId, User mentor) {
        // Find classroom by ID
        Optional<Classroom> classroomOpt = classroomRepository.findById(classroomId);
        
        // Check if classroom exists and is owned by the mentor
        if (classroomOpt.isEmpty() || !classroomOpt.get().getMentor().equals(mentor)) {
            return List.of();
        }
        
        Classroom classroom = classroomOpt.get();
        
        // Map students to MentorProgressStudent
        return classroom.getStudents().stream()
            .map(student -> {
                // Create UserResponseDTO
                UserResponseDTO res = userMapper.toResponseDTO(student);
                Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(student.getId());
                if (avatar.isPresent()) {
                    res.setAvatar(avatar.get().getImageUrl());
                }
                
                // Calculate sequence (streak)
                int sequence = getSequenceForStudent(student.getId());
                
                // Calculate initiated courses
                int initiatedCourses = getInitiatedCoursesForStudent(student);
                
                // Create MentorProgressStudent DTO
                return new MentorProgressStudent(res, sequence, initiatedCourses);
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    private int getSequenceForStudent(String studentId) {
        User student = userRepository.findById(studentId).orElseThrow();
        // Find experience events for the student
        List<ExperienceEvent> events = experienceEventRepository.findByUserOrderByCreatedAtDesc(student);
        if (events.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate expectedDate = today;

        // Iterate through events to find consecutive days
        for (ExperienceEvent event : events) {
            LocalDate eventDate = event.getCreatedAt().toLocalDate();
            if (eventDate.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (eventDate.isBefore(expectedDate)) {
                break; // Break if there's a gap in the streak
            }
        }

        return streak;
    }
    
    @Transactional(readOnly = true)
    private int getInitiatedCoursesForStudent(User student) {
        // Find watch events for the student
        List<CourseLessonContentWatchEvent> watchEvents = courseLessonContentWatchEventRepository.findByStudent(student);
        
        // Extract unique courses from watch events
        Set<String> uniqueCourseIds = watchEvents.stream()
            .map(event -> event.getCourseLessonContent().getCourseLesson().getCourse().getId())
            .collect(Collectors.toSet());
        
        return uniqueCourseIds.size();
    }
}