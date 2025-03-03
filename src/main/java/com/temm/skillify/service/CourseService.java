package com.temm.skillify.service;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCoursesByCurrentMentor() {
        User currentUser = getCurrentUser();
        return courseRepository.findByCreator(currentUser);
    }

    public Course getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
        
        User currentUser = getCurrentUser();
        if (!course.getCreator().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this course");
        }
        
        return course;
    }

    public Course createCourse(Course course) {
        User currentUser = getCurrentUser();
        course.setCreator(currentUser);
        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course updatedCourse) {
        Course existingCourse = getCourseById(id);
        
        // Update fields while preserving creator
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setLevel(updatedCourse.getLevel());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setImageUrl(updatedCourse.getImageUrl());
        existingCourse.setCategories(updatedCourse.getCategories());
        
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(String id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}