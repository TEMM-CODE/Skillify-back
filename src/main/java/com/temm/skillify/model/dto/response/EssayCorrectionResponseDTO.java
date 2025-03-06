package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;
import com.temm.skillify.model.enums.EssayConquest;

@Data
@EqualsAndHashCode(callSuper = true)
public class EssayCorrectionResponseDTO extends BaseResponseDTO {
    private EssayResponseDTO essay;
    private UserResponseDTO mentor;
    private EssayExecutionResponseDTO essayExecution;
    private String estruturaCoesaoComentario;
    private String argumentacaoComentario;
    private Set<EssayConquest> conquistas;
    private Integer competencia1Score;
    private Integer competencia2Score;
    private Integer competencia3Score;
    private Integer competencia4Score;
    private Integer competencia5Score;
}