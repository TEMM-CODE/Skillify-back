package com.temm.skillify.repository;

import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseLessonContentWatchEventRepository extends JpaRepository<CourseLessonContentWatchEvent, String> {

    List<CourseLessonContentWatchEvent> findByStudent(User student);

    Optional<CourseLessonContentWatchEvent> findByIdAndStudent(String id, User student);
}
