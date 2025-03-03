package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLessonCategory;

import java.util.List;

@Repository
public interface CourseLessonCategoryRepository extends JpaRepository<CourseLessonCategory, String> {
    List<CourseLessonCategory> findByCourse(Course course);
}
