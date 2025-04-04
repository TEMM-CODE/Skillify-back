package com.temm.skillify.repository;


import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.QuestionContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionContentRepository extends JpaRepository<QuestionContent, String> {

    // Find all QuestionContent entities by their associated Question
    List<QuestionContent> findByQuestion(Question question);

    // Find all QuestionContent entities by Question ID (optional, if needed)
    List<QuestionContent> findByQuestionId(String questionId);
}