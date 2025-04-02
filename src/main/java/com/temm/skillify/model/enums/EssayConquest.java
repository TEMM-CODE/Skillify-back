package com.temm.skillify.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EssayConquest {
    ARGUMENTACAO_SOLIDA("ARGUMENTACAO_SOLIDA"),
    COESAO_PERFEITA("COESAO_PERFEITA"),
    VOCABULARIO_RICO("VOCABULARIO_RICO");

    private final String value;

    EssayConquest(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EssayConquest fromValue(String value) {
        for (EssayConquest conquest : EssayConquest.values()) {
            if (conquest.value.equalsIgnoreCase(value)) {
                return conquest;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}