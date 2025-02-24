package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, String> {
}
