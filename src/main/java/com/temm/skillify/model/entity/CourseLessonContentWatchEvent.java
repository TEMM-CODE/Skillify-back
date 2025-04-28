package com.temm.skillify.model.entity;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.CourseLessonContentType;

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
public class CourseLessonContentWatchEvent  extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private CourseLessonContent courseLessonContent;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User student;


    
}
