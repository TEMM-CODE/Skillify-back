package com.temm.skillify.model.entity;

import java.util.Set;

import com.temm.skillify.model.categories.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PracticeExecution extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    private Practice practice;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Option> selectedAnswers;

    private Long correctAnswers;

    private Long duration;
}
