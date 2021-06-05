package com.example.api.bots;

import com.example.message.DevBotResponse;
import org.jetbrains.annotations.NotNull;

public interface BotResponseSender {

    boolean sendDevBotResponse(@NotNull final DevBotResponse DevBotResponse);

}
