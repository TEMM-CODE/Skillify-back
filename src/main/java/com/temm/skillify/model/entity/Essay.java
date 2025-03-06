package com.temm.skillify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.temm.skillify.model.categories.BaseEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Essay extends BaseEntity {
    
    private String theme;
    
    @Column(length = 2000)
    private String description;
    
    private Integer minWords;
    private LocalDateTime maxDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Classroom classroom;
}