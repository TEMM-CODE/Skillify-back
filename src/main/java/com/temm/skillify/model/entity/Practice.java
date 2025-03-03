package com.temm.skillify.model.entity;


import com.temm.skillify.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Practice extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Classroom classroom;
    
    private String title;
    private Integer numberOfQuestions;
    private Integer duracao; // in minutes
    private LocalDateTime openingDate;
    private LocalDateTime maximumDate;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Question> questions;
}