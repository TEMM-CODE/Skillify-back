package com.temm.skillify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.ProcessedAsaasEvent;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedAsaasEvent, String> {
    boolean existsByEventId(String eventId);
}