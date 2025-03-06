package com.temm.skillify.model.dto.request;


import lombok.Data;
import java.util.Set;
import com.temm.skillify.model.enums.EssayConquest;

@Data
public class EssayCorrectionCreateDTO {
    private String essayId;
    private String mentorId;
    private String essayExecutionId;
    private String estruturaCoesaoComentario;
    private String argumentacaoComentario;
    private Set<EssayConquest> conquistas;
    private Integer competencia1Score;
    private Integer competencia2Score;
    private Integer competencia3Score;
    private Integer competencia4Score;
    private Integer competencia5Score;
}
