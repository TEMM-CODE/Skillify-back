package com.temm.skillify.service;

import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseLessonCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.CourseLessonCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.CourseLessonCategoryRepository;
import com.temm.skillify.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CourseLessonCategoryStudentService {

    @Autowired
    private CourseLessonCategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseLessonCategoryResponseDTO> getCategoriesByCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + courseId));

        List<CourseLessonCategory> categories = categoryRepository.findByCourse(course);
        return categories.stream()
                .map(this::toCourseLessonCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseLessonCategoryResponseDTO getCategoryById(String id) {
        CourseLessonCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        return toCourseLessonCategoryResponseDTO(category);
    }

    private CourseLessonCategoryResponseDTO toCourseLessonCategoryResponseDTO(CourseLessonCategory category) {
        CourseLessonCategoryResponseDTO dto = new CourseLessonCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCourse(toCourseResponseDTO(category.getCourse()));
        return dto;
    }

    private CourseResponseDTO toCourseResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        dto.setCreator(toUserResponseDTO(course.getCreator()));
        
        if (course.getCategories() != null) {
            dto.setCategories(course.getCategories().stream()
                    .map(this::toCourseCategoryResponseDTO)
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }

    private CourseCategoryResponseDTO toCourseCategoryResponseDTO(CourseCategory category) {
        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        // Add other user fields as needed
        return dto;
    }
}