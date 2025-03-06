package com.temm.skillify.model.entity;


import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.EssayConquest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EssayCorrection extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Essay essay;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
    
    @OneToOne(fetch = FetchType.EAGER)
    private EssayExecution essayExecution;
    
    @Column(length = 1000)
    private String estruturaCoesaoComentario;
    
    @Column(length = 1000)
    private String argumentacaoComentario;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<EssayConquest> conquistas;
    
    private Integer competencia1Score;
    private Integer competencia2Score;
    private Integer competencia3Score;
    private Integer competencia4Score;
    private Integer competencia5Score;
}
