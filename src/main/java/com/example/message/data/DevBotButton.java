package com.example.message.data;

import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * @author Farrukh Karimov
 */
public class DevBotButton {
    private String buttonText;
    private String buttonHiddenText;
    private boolean requiredLocation;

    public DevBotButton(@NotNull final String buttonText, @NotNull final String buttonHiddenText) {
        this.buttonText = buttonText;
        this.buttonHiddenText = buttonHiddenText;
        this.requiredLocation = false;
    }

    public DevBotButton() {
        buttonHiddenText = "";
        buttonHiddenText = "";
        requiredLocation = false;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonHiddenText() {
        return buttonHiddenText;
    }

    public void setButtonHiddenText(String buttonHiddenText) {
        this.buttonHiddenText = buttonHiddenText;
    }

    public boolean isRequiredLocation() {
        return requiredLocation;
    }

    public void setRequiredLocation(boolean requiredLocation) {
        this.requiredLocation = requiredLocation;
    }
}
