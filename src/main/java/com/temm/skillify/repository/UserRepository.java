package com.temm.skillify.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    Page<User> findByIdIn(List<String> ids, Pageable pageable);
    List<User> findByIdIn(List<String> ids, Sort sort);
}
