package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.User;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, String> {
    List<Practice> findByMentor(User mentor);
    List<Practice> findByClassroom(Classroom classroom);
    List<Practice> findByClassroomIn(List<Classroom> classrooms);
}