package com.temm.skillify.model.enums;

public enum BackgroundColorEnum {
    DARK_BLUE("#1E2A38"),
    BLACK("#000000"),
    WHITE("#FFFFFF");

    private final String hex;

    BackgroundColorEnum(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    @Override
    public String toString() {
        return hex;
    }

    public static BackgroundColorEnum fromString(String text) {
        for (BackgroundColorEnum color : BackgroundColorEnum.values()) {
            if (color.hex.equalsIgnoreCase(text) || color.name().equalsIgnoreCase(text)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Unknown background color: " + text);
    }
}