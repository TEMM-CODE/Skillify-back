package com.temm.skillify.model.repository;

import com.temm.skillify.enums.TypeUser;
import com.temm.skillify.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findByTypeUser(TypeUser typeUser);
}
