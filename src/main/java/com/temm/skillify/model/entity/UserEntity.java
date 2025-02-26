package com.temm.skillify.model.entity;

import com.temm.skillify.enums.TypeUser;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@Entity
public class UserEntity {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(length = 36)
    private String id;

    private String name;
    private String email;
    private String phone;
    private String biography;
    private TypeUser typeUser;
    private Boolean emailNotification;
    private Boolean pushNotifications;
    private Boolean weekReport;
    private Boolean studyReminder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
