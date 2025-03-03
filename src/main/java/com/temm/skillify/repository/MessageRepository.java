package com.temm.skillify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.Message;
import com.temm.skillify.model.entity.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByRemetente(User remetente);
    List<Message> findByDestinatario(User destinatario);
}
