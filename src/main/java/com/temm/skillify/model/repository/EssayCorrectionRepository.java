package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.EssayCorrection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssayCorrectionRepository extends JpaRepository<EssayCorrection, String> {
}
