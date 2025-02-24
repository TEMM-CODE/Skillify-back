package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.SimulatedResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulatedResultRepository extends JpaRepository<SimulatedResult, String> {
}
