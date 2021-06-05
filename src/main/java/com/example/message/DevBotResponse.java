package com.example.message;

import com.example.message.data.DevBotButton;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Farrukh Karimov
 */
public class DevBotResponse {
    private String receiverChatId;
    private String message;
    private List<List<DevBotButton>> buttonsMatrix;
    private boolean inlineButtons = true; // use inline buttons by default

    public DevBotResponse() {
        buttonsMatrix = new LinkedList<>();
        buttonsMatrix.add(new LinkedList<>());
    }

    /** Method to add button to the last row of buttons */
    public DevBotResponse addButton(@NotNull final DevBotButton DevBotButton) {
        buttonsMatrix.get(buttonsMatrix.size() - 1).add(DevBotButton);
        return this;
    }

    /** Method to set new row of buttons */
    public DevBotResponse setNewButtonsLine() {
        buttonsMatrix.add(new LinkedList<>());
        return this;
    }

    public void cleanButtons() {
        buttonsMatrix.clear();
        setNewButtonsLine();
    }

    public boolean hasButtons() {
        return getButtonsMatrix().size() > 1 || getButtonsMatrix().get(0).size() > 0;
    }

    public List<List<DevBotButton>> getButtonsMatrix() {
        final List<List<DevBotButton>> result = new LinkedList<>();
        for (List<DevBotButton> buttonsRow : buttonsMatrix ) {
            result.add(new LinkedList<>(buttonsRow));
        }
        return result;
    }

    public String getReceiverChatId() {
        return receiverChatId;
    }

    public void setReceiverChatId(String receiverChatId) {
        this.receiverChatId = receiverChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull final String message) {
        this.message = message;
    }


    public boolean isInlineButtons() {
        return inlineButtons;
    }

    public void setInlineButtons(boolean inlineButtons) {
        this.inlineButtons = inlineButtons;
    }

}
