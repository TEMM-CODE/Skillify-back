package com.temm.skillify.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Goal;
import com.temm.skillify.model.enums.GoalType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, String> {
    List<Goal> findByClassroomsContaining(Classroom classroom);
    List<Goal> findByType(GoalType type);
    List<Goal> findByOpeningDateBeforeAndFinalDateAfter(LocalDateTime currentDate, LocalDateTime currentDate2);
    List<Goal> findByFinalDateBefore(LocalDateTime date);
    List<Goal> findByClassroomsInAndFinalDateAfter(List<Classroom> classrooms, LocalDateTime date);
}