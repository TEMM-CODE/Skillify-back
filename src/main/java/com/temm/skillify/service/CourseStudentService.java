package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.*;
import com.temm.skillify.model.entity.*;
import com.temm.skillify.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final CourseRepository courseRepository;
    private final CourseLessonCategoryRepository courseLessonCategoryRepository;
    private final CourseLessonRepository courseLessonRepository;
    private final UserService userService;
    private final ClassroomRepository classroomRepository;

    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseResponseDTO getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        return mapToCourseResponseDTO(course);
    }

    public List<CourseResponseDTO> getEnrolledCourses(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);
        
        // Get classrooms where the student is enrolled
        List<Classroom> classrooms = classroomRepository.findAll().stream()
                .filter(classroom -> classroom.getStudents().contains(student))
                .collect(Collectors.toList());
        
        // Get courses through course lessons associated with these classrooms
        Set<Course> enrolledCourses = new HashSet<>();
        for (Classroom classroom : classrooms) {
            List<CourseLesson> lessons = courseLessonRepository.findAll().stream()
                    .filter(lesson -> lesson.getClassroom() != null && lesson.getClassroom().getId().equals(classroom.getId()))
                    .collect(Collectors.toList());
            
            for (CourseLesson lesson : lessons) {
                enrolledCourses.add(lesson.getCourse());
            }
        }
        
        return enrolledCourses.stream()
                .map(this::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseLessonCategoryResponseDTO> getCourseLessonCategories(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        List<CourseLessonCategory> categories = courseLessonCategoryRepository.findByCourse(course);
        return categories.stream()
                .map(this::mapToCourseLessonCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseLessonResponseDTO> getCourseLessons(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        List<CourseLesson> lessons = courseLessonRepository.findByCourse(course);
        return lessons.stream()
                .map(this::mapToCourseLessonResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseLessonResponseDTO> getLessonsByCategory(String categoryId) {
        CourseLessonCategory category = courseLessonCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Course lesson category not found with id: " + categoryId));
        
        List<CourseLesson> lessons = courseLessonRepository.findByCourseLessonCategory(category);
        return lessons.stream()
                .map(this::mapToCourseLessonResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void enrollInCourse(String courseId, Authentication authentication) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        
        User student = userService.getUserFromAuthentication(authentication);
        
        // Assuming there's a default or general classroom for the course
        // In a real application, you might need more complex logic here
        List<Classroom> classrooms = classroomRepository.findAll().stream()
                .filter(classroom -> {
                    List<CourseLesson> lessons = courseLessonRepository.findByCourse(course);
                    return lessons.stream().anyMatch(lesson -> 
                            lesson.getClassroom() != null && lesson.getClassroom().getId().equals(classroom.getId()));
                })
                .collect(Collectors.toList());
        
        if (classrooms.isEmpty()) {
            throw new EntityNotFoundException("No classroom found for this course");
        }
        
        // Enroll student in the first available classroom
        Classroom classroom = classrooms.get(0);
        Set<User> students = classroom.getStudents();
        students.add(student);
        classroom.setStudents(students);
        
        classroomRepository.save(classroom);
    }

    private CourseResponseDTO mapToCourseResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        
        // Map creator information
        UserResponseDTO creatorDTO = new UserResponseDTO();
        creatorDTO.setId(course.getCreator().getId());
        creatorDTO.setName(course.getCreator().getName());
        creatorDTO.setEmail(course.getCreator().getEmail());
        dto.setCreator(creatorDTO);
        
        // Map categories
        Set<CourseCategoryResponseDTO> categoryDTOs = course.getCategories().stream()
                .map(category -> {
                    CourseCategoryResponseDTO categoryDTO = new CourseCategoryResponseDTO();
                    categoryDTO.setId(category.getId());
                    categoryDTO.setCategoryName(category.getCategoryName());
                    categoryDTO.setCreatedAt(category.getCreatedAt());
                    categoryDTO.setUpdatedAt(category.getUpdatedAt());
                    return categoryDTO;
                })
                .collect(Collectors.toSet());
        
        dto.setCategories(categoryDTOs);
        
        return dto;
    }

    private CourseLessonCategoryResponseDTO mapToCourseLessonCategoryResponseDTO(CourseLessonCategory category) {
        CourseLessonCategoryResponseDTO dto = new CourseLessonCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        courseDTO.setId(category.getCourse().getId());
        courseDTO.setName(category.getCourse().getName());
        dto.setCourse(courseDTO);
        
        return dto;
    }

    private CourseLessonResponseDTO mapToCourseLessonResponseDTO(CourseLesson lesson) {
        CourseLessonResponseDTO dto = new CourseLessonResponseDTO();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDuration(lesson.getDuration());
        dto.setFiles(lesson.getFiles());
        dto.setCreatedAt(lesson.getCreatedAt());
        dto.setUpdatedAt(lesson.getUpdatedAt());
        
        // Map course information
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        courseDTO.setId(lesson.getCourse().getId());
        courseDTO.setName(lesson.getCourse().getName());
        dto.setCourse(courseDTO);
        
        // Map category information if available
        if (lesson.getCourseLessonCategory() != null) {
            CourseLessonCategoryResponseDTO categoryDTO = new CourseLessonCategoryResponseDTO();
            categoryDTO.setId(lesson.getCourseLessonCategory().getId());
            categoryDTO.setName(lesson.getCourseLessonCategory().getName());
            dto.setCourseLessonCategory(categoryDTO);
        }
        
        // Map classroom information if available
        if (lesson.getClassroom() != null) {
            ClassroomResponseDTO classroomDTO = new ClassroomResponseDTO();
            classroomDTO.setId(lesson.getClassroom().getId());
            classroomDTO.setName(lesson.getClassroom().getName());
            dto.setClassroom(classroomDTO);
        }
        
        return dto;
    }
}