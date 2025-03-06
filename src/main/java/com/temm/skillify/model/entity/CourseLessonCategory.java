package com.temm.skillify.model.entity;

import com.temm.skillify.model.categories.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseLessonCategory extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    
    private String name;
}