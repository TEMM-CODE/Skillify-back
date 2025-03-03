package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Question;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, String> {
    List<Option> findByQuestion(Question question);
    List<Option> findByQuestionAndCorrect(Question question, Boolean correct);
}