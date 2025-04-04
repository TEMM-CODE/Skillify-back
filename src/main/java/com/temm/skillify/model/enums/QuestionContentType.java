package com.temm.skillify.model.enums;

public enum QuestionContentType {
    TEXT("text"),
    IMAGE("image");

    private final String value;

    QuestionContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static QuestionContentType fromString(String text) {
        for (QuestionContentType type : QuestionContentType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type: " + text);
    }
}
