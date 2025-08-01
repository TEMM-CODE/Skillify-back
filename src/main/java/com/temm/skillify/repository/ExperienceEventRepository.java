package com.temm.skillify.repository;

import com.temm.skillify.model.entity.ExperienceEvent;
import com.temm.skillify.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ExperienceEventRepository extends JpaRepository<ExperienceEvent, String> {
    // Find all experience events for a specific user
    List<ExperienceEvent> findByUser(User user);

    // Find all experience events for a specific user ordered by creation date
    List<ExperienceEvent> findByUserOrderByCreatedAtDesc(User user);

    List<ExperienceEvent> findByUserIdInAndCreatedAtBetween(Set<String> userIds, LocalDateTime start, LocalDateTime end);
}