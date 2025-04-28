package com.temm.skillify.repository;


import com.temm.skillify.model.entity.CourseLessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseLessonContentRepository extends JpaRepository<CourseLessonContent, String> {
    
    // Find all content for a specific course lesson
    List<CourseLessonContent> findByCourseLessonId(String courseLessonId);
    
    // Find all content for a specific course lesson ordered by position
    List<CourseLessonContent> findByCourseLessonIdOrderByPositionAsc(String courseLessonId);
    
    // Delete all content for a specific course lesson
    void deleteByCourseLessonId(String courseLessonId);
    
    // Find content by course lesson and position
    CourseLessonContent findByCourseLessonIdAndPosition(String courseLessonId, int position);
}