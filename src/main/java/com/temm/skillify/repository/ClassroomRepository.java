package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, String> {
    List<Classroom> findByMentor(User mentor);
    Optional<Classroom> findByIdAndMentor(String id, User mentor);
}
