package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {
}
