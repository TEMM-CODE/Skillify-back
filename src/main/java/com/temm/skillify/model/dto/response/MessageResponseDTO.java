package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageResponseDTO extends BaseResponseDTO {
    private UserResponseDTO remetente;
    private UserResponseDTO destinatario;
    private String content;
}
