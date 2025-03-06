package com.temm.skillify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.temm.skillify.model.categories.BaseEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<CourseCategory> categories;
    
    private String level;
    private String name;
    
    @Column(length = 2000)
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User creator;
    
    private Integer duration; // in minutes
    
    private String imageUrl;
}
