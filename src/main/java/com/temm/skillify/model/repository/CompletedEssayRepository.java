package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.CompletedEssay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedEssayRepository extends JpaRepository<CompletedEssay, String> {
}
