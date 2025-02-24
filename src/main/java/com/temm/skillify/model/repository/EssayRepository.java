package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Essay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssayRepository extends JpaRepository<Essay, String> {
}
