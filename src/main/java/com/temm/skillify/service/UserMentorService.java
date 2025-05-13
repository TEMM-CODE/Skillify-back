package com.temm.skillify.service;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.response.MentorProgressStudent;
import com.temm.skillify.model.dto.response.StudentRankingPositionDTO;
import com.temm.skillify.model.dto.response.StudentRankingResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.entity.UserAvatar;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.CourseLessonContentWatchEventRepository;
import com.temm.skillify.repository.ExperienceEventRepository;
import com.temm.skillify.repository.UserAvatarRepository;
import com.temm.skillify.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

     public List<StudentRankingResponseDTO> getStudentRankingsForAllClassrooms(int page, int size) {
        User currentMentor = getCurrentUser();

        // Find all classrooms where the user is a mentor
        List<Classroom> classrooms = classroomRepository.findByMentor(currentMentor);

        // Generate ranking for each classroom
        return classrooms.stream().map(classroom -> {
            // Get paginated ranking of students by XP
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "xp"));
            Page<User> studentPage = userRepository.findByIdIn(
                    classroom.getStudents().stream().map(User::getId).collect(Collectors.toList()),
                    pageable
            );

            // Build ranking
            List<StudentRankingPositionDTO> ranking = studentPage.getContent().stream()
                    .map(user -> {
                        StudentRankingPositionDTO positionDTO = new StudentRankingPositionDTO();
                        positionDTO.userId = user.getId();
                        positionDTO.userName = user.getName();
                        positionDTO.xpAmount = user.getXp();
                        Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(user.getId());
                        positionDTO.avatar = avatar.map(UserAvatar::getImageUrl).orElse(null);
                        positionDTO.position = (int) (studentPage.getNumber() * studentPage.getSize() + studentPage.getContent().indexOf(user) + 1);
                        return positionDTO;
                    })
                    .collect(Collectors.toList());

            // Find the authenticated mentor's position (not applicable, so set to 0 or handle differently if needed)
            int yourPosition = 0; // Mentors typically don't have a position in student rankings

            // Build response
            StudentRankingResponseDTO response = new StudentRankingResponseDTO();
            response.classroomId = classroom.getId();
            response.classroomName = classroom.getName();
            response.ranking = ranking;
            response.yourPosition = yourPosition;

            return response;
        }).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

       @Transactional
    public UserResponseDTO updateStudentProfileByMentor(String studentId, RegisterRequest request, User mentor) {
        // Find the student
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Verify mentor has authority (student is in one of mentor's classrooms)
        List<Classroom> mentorClassrooms = classroomRepository.findByMentor(mentor);
        boolean isStudentInClassroom = mentorClassrooms.stream()
            .anyMatch(classroom -> classroom.getStudents().contains(student));
        if (!isStudentInClassroom) {
            throw new IllegalArgumentException("Mentor does not have authority to edit this student");
        }

        // Update student fields if provided
        if (request.getName() != null) {
            student.setName(request.getName());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail());
        }
        if (request.getTel() != null) {
            student.setTel(request.getTel());
        }
        if (request.getBiography() != null) {
            student.setBiography(request.getBiography());
        }
        student.setEmailNotifications(request.isEmailNotifications());
        student.setPushNotifications(request.isPushNotifications());
        student.setWeeklyReport(request.isWeeklyReport());
        student.setStudyReminder(request.isStudyReminder());

        // Save updated student
        User updatedStudent = userRepository.save(student);
        return userMapper.toResponseDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudentByMentor(String studentId, User mentor) {
        // Find the student
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Verify mentor has authority (student is in one of mentor's classrooms)
        List<Classroom> mentorClassrooms = classroomRepository.findByMentor(mentor);
        /*boolean isStudentInClassroom = mentorClassrooms.stream()
            .anyMatch(classroom -> classroom.getStudents().contains(student));
        if (!isStudentInClassroom) {
            throw new IllegalArgumentException("Mentor does not have authority to delete this student");
        } */

        // Remove student from all mentor's classrooms
        mentorClassrooms.forEach(classroom -> classroom.getStudents().remove(student));
        classroomRepository.saveAll(mentorClassrooms);

        // Optionally delete the student entirely if they are not in other classrooms
        List<Classroom> allClassrooms = classroomRepository.findAll();
        boolean isStudentInOtherClassrooms = allClassrooms.stream()
            .anyMatch(classroom -> classroom.getStudents().contains(student));
        if (!isStudentInOtherClassrooms) {
            userRepository.delete(student);
        }
    }

        @Transactional
    public UserResponseDTO createStudent(RegisterRequest request, User mentor) {
        // Validate required fields
        if (request.getName() == null || request.getEmail() == null) {
            throw new IllegalArgumentException("Name and email are required");
        }

        // Check if email is already in use
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create new student
        User student = new User();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setTel(request.getTel() != null ? request.getTel() : "");
        student.setBiography(request.getBiography() != null ? request.getBiography() : "");
        student.setEmailNotifications(request.isEmailNotifications());
        student.setPushNotifications(request.isPushNotifications());
        student.setWeeklyReport(request.isWeeklyReport());
        student.setStudyReminder(request.isStudyReminder());
        student.setXp(0); // Default XP
        student.setRole(UserRole.ESTUDANTE);

        // Save student
        User savedStudent = userRepository.save(student);

        return userMapper.toResponseDTO(savedStudent);
    }

}