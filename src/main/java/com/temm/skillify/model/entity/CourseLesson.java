package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.temm.skillify.model.categories.BaseEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseLesson extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private CourseLessonCategory courseLessonCategory;
    
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> files;
    
    private String name;
    private Integer duration; // in minutes
}