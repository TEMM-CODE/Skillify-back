package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, String> {
    Optional<CourseCategory> findByCategoryName(String categoryName);

       // Find all categories created by a specific user
       List<CourseCategory> findByCreatedBy(User user);
    
       // Find a category by ID and created by user
       Optional<CourseCategory> findByIdAndCreatedBy(String id, User user);
       
       // Find a category by name and created by user
       Optional<CourseCategory> findByCategoryNameAndCreatedBy(String categoryName, User user);
       
}