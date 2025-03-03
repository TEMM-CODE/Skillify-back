package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Course;
import com.temm.skillify.model.entity.CourseLesson;
import com.temm.skillify.model.entity.CourseLessonCategory;

import java.util.List;

@Repository
public interface CourseLessonRepository extends JpaRepository<com.temm.skillify.model.entity.CourseLesson, String> {
    List<CourseLesson> findByCourse(Course course);
    List<CourseLesson> findByCourseLessonCategory(CourseLessonCategory category);
}