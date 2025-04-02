package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssayExecutionRepository extends JpaRepository<EssayExecution, String> {
    List<EssayExecution> findByStudent(User student);
    Optional<EssayExecution> findByIdAndStudent(String id, User student);
    List<EssayExecution> findByEssayClassroomIdIn(List<String> classroomIds);
}
