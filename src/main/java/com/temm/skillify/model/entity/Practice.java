package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.categories.Challengeable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Practice extends BaseEntity implements Challengeable{
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Classroom classroom;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Course> courses;
    
    private String title;
    private Integer numberOfQuestions;
    private Integer duracao; // in minutes
    private LocalDateTime openingDate;
    private LocalDateTime maximumDate;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Question> questions;

    private Integer numberOfAllowedAttempts;
}