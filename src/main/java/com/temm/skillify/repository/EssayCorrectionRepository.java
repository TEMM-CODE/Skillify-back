package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssayCorrectionRepository extends JpaRepository<EssayCorrection, String> {
    List<EssayCorrection> findByMentor(User mentor);
    List<EssayCorrection> findByEssay(Essay essay);
    Optional<EssayCorrection> findByEssayExecution(EssayExecution execution);
}
