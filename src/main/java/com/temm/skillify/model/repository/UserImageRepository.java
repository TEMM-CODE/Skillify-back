package com.temm.skillify.model.repository;

import com.temm.skillify.model.entity.UserEntity;
import com.temm.skillify.model.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, String> {

    UserImage findByUser(UserEntity user);
}
