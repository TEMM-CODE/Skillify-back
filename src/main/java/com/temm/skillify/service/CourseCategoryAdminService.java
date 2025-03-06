package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.repository.CourseCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseCategoryAdminService {
    
    private final CourseCategoryRepository courseCategoryRepository;
    
    public List<CourseCategory> findAll() {
        return courseCategoryRepository.findAll();
    }
    
    public Optional<CourseCategory> findById(String id) {
        return courseCategoryRepository.findById(id);
    }
    
    public Optional<CourseCategory> findByCategoryName(String categoryName) {
        return courseCategoryRepository.findByCategoryName(categoryName);
    }
    
    public CourseCategory save(CourseCategory courseCategory) {
        return courseCategoryRepository.save(courseCategory);
    }
    
    public void deleteById(String id) {
        courseCategoryRepository.deleteById(id);
    }
}