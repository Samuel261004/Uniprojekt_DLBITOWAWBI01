package com.example.demo;

public class UserSettingsDto {

    private String fontSize;
    private boolean highContrast;

    public String getFontSize() {
        return fontSize;
    }

    public boolean isHighContrast() {
        return highContrast;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }
}