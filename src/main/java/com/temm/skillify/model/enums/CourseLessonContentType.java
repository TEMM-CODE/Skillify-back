package com.temm.skillify.model.enums;

public enum CourseLessonContentType {
    TEXT("text"),
    IMAGE("image"),
    VIDEO("video");

    private final String value;

    CourseLessonContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CourseLessonContentType fromString(String text) {
        for (CourseLessonContentType type : CourseLessonContentType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type: " + text);
    }
}
