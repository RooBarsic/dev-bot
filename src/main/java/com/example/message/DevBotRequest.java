package com.example.message;

import com.example.message.data.Location;
import com.example.message.data.UiPlatform;
import org.jetbrains.annotations.NotNull;


/**
 *
 *
 * @author Farrukh Karimov
 */

public class DevBotRequest {
    private String userChatId;
    private String message;
    private UiPlatform uiPlatform;
    private Location location;
    private boolean hasLocation;

    public DevBotRequest(@NotNull final String userChatId,
                         @NotNull final String message,
                         @NotNull final UiPlatform uiPlatform) {
        this.message = message;
        this.userChatId = userChatId;
        this.uiPlatform = uiPlatform;
    }

    public DevBotRequest() {
        this.message = "null";
        this.userChatId = "null";
        this.location = new Location();
        this.hasLocation = false;
        this.uiPlatform = UiPlatform.DEFAULT;
    }

    public String getUserChatId() {
        return userChatId;
    }

    public void setUserChatId(String userChatId) {
        this.userChatId = userChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UiPlatform getUiPlatform() {
        return uiPlatform;
    }

    public void setUiPlatform(UiPlatform uiPlatform) {
        this.uiPlatform = uiPlatform;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        this.hasLocation = hasLocation;
    }
}
