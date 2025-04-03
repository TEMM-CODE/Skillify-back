package com.temm.skillify.repository;


import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.PracticeExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeExecutionRepository extends JpaRepository<PracticeExecution, String> {
    
    // Find all practice executions by a list of practices
    List<PracticeExecution> findByPracticeIn(List<Practice> practices);
    
    // Optional: Additional useful queries
    
    // Find all practice executions for a specific practice
    List<PracticeExecution> findByPractice(Practice practice);
    
    // Find all practice executions for a specific student
    List<PracticeExecution> findByStudentId(String studentId);
    
    // Find all practice executions with a minimum number of correct answers
    List<PracticeExecution> findByCorrectAnswersGreaterThanEqual(Long correctAnswers);

    long countByStudentIdAndPracticeId(String studentId, String practiceId);
}