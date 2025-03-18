package com.temm.skillify.model.entity;


import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.GoalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Goal extends BaseEntity {
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Classroom> classrooms;
    
    private Integer number;
    
    @Enumerated(EnumType.STRING)
    private GoalType type;
    
    private LocalDateTime openingDate;
    private LocalDateTime finalDate;
}