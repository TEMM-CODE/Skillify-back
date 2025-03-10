package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.request.CourseCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.CourseResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseResponseDTO toResponseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        dto.setCreator(toUserResponseDTO(course.getCreator()));
        dto.setCategories(toCourseCategoryResponseDTOSet(course.getCategories()));
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        
        return dto;
    }

    public Course toEntity(CourseCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setLevel(dto.getLevel());
        course.setDuration(dto.getDuration());
        course.setImageUrl(dto.getImageUrl());
        
        // Note: Categories and creator should be set separately using service logic
        
        return course;
    }

    public void updateEntityFromDTO(Course course, CourseCreateDTO dto) {
        if (course == null || dto == null) {
            return;
        }
        
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setLevel(dto.getLevel());
        course.setDuration(dto.getDuration());
        course.setImageUrl(dto.getImageUrl());
        
        // Note: Categories should be updated separately using service logic
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTel(user.getTel());
        dto.setBiography(user.getBiography());
        dto.setEmailNotifications(user.isEmailNotifications());
        dto.setPushNotifications(user.isPushNotifications());
        dto.setWeeklyReport(user.isWeeklyReport());
        dto.setStudyReminder(user.isStudyReminder());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }

    private CourseCategoryResponseDTO toCourseCategoryResponseDTO(CourseCategory category) {
        if (category == null) {
            return null;
        }

        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        return dto;
    }

    private Set<CourseCategoryResponseDTO> toCourseCategoryResponseDTOSet(Set<CourseCategory> categories) {
        if (categories == null) {
            return null;
        }

        return categories.stream()
                .map(this::toCourseCategoryResponseDTO)
                .collect(Collectors.toSet());
    }
}