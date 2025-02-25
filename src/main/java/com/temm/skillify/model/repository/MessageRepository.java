package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,String> {
}
