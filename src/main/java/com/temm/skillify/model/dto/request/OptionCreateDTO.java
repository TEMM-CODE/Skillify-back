package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class OptionCreateDTO {
    private String questionId;
    private String title;
    private Boolean correct;
}
