package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository <Class, String> {
}
