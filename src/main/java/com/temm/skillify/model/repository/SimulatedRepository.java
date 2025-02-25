package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Simulated;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulatedRepository extends JpaRepository<Simulated, String> {
}
