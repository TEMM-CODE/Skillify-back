package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class MessageCreateDTO {
    private String remetenteId;
    private String destinatarioId;
    private String content;
}