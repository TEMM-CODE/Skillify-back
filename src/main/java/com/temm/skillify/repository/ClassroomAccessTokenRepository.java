package com.temm.skillify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.ClassroomAccessToken;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomAccessTokenRepository extends JpaRepository<ClassroomAccessToken, String> {
    List<ClassroomAccessToken> findByClassroom(Classroom classroom);
    Optional<ClassroomAccessToken> findByToken(String token);
}
