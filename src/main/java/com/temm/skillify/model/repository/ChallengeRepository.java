package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, String> {
}
