package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Essay;

import java.util.List;

@Repository
public interface EssayRepository extends JpaRepository<Essay, String> {
    List<Essay> findByClassroom(Classroom classroom);
}