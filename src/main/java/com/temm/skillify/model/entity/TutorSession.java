package com.temm.skillify.model.entity;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.SessionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TutorSession extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
    
    private String title;
    private LocalDate date;
    private String dateHour;
    
    @Enumerated(EnumType.STRING)
    private SessionType type;
    
    private String link;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User student;
}
