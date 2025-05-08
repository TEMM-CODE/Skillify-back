package com.temm.skillify.repository;

import com.temm.skillify.model.entity.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, String> {
    Optional<UserAvatar> findByUserId(String userId);
}