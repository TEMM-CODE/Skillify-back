package com.temm.skillify.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.entity.GoalExecution;
import com.temm.skillify.model.entity.User;

@Repository
public interface GoalExecutionRepository extends JpaRepository<GoalExecution, String> {
     Optional<GoalExecution> findByGoalAndStudent(Goal goal, User student);
     List<GoalExecution> findByStudent(User student);
}