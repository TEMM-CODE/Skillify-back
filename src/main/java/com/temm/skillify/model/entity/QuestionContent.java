package com.temm.skillify.model.entity;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.QuestionContentType;

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
public class QuestionContent extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private int position; // Determines the order of elements

    @Enumerated(EnumType.STRING)
    private QuestionContentType type; // "TEXT" or "IMAGE"


    @Column(name = "\"value\"") 
    private String value; // Holds either text or an image URL

    // Getters and setters
}
