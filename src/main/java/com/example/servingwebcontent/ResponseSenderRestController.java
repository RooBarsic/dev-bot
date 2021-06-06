package com.example.servingwebcontent;


import com.example.message.DevBotResponse;
import com.example.message.data.DevBotButton;
import com.example.message.data.UserActivityStatus;
import com.example.servingwebcontent.components.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResponseSenderRestController {
    private final TelegramBot telegramBot;

    @Autowired
    ResponseSenderRestController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/send/telegram", method = RequestMethod.GET)
    public String sendTelegramMessage(@RequestParam(name = "chatId", required = false, defaultValue = "") String chatId,
                                      @RequestParam(name = "message", required = false, defaultValue = "") String message) {
        String response = "";
        if (chatId.equals("")) {
            response += "Error: No chatId\n";
        }
        if (message.equals("")) {
            response += "Error: No message to send\n";
        }
        if (response.length() == 0) {
            final DevBotResponse DevBotResponse = new DevBotResponse();
            DevBotResponse.setReceiverChatId(chatId);
            DevBotResponse.setMessage(message);
            telegramBot.getResponseSender()
                    .sendDevBotResponse(DevBotResponse);
            response += "OK";
        }
        return response;
    }

    @RequestMapping(value = "/send/telegram", method = RequestMethod.POST)
    public String sendTelegramMessage(@RequestBody DevBotResponse response) {
        System.out.println("----- Telegram sender : got new request from POST");
        String responseStatus = "";
        if (response.getReceiverChatId().equals("")) {
            responseStatus += "Error: No chatId\n";
        }
        if (response.getMessage().equals("")) {
            responseStatus += "Error: No message to send\n";
        }
        if (responseStatus.length() == 0) {
            if (!response.hasButtons() && response.isNoButtons() == false) {
//                response.addButton(new DevBotButton("help", "/help"));
//                response.addButton(new DevBotButton("feedback", "/feedback"));
//                response.setInlineButtons(true);
                if (response.isHasActivityStatus()) {
                    if (response.getUserActivityStatus() == UserActivityStatus.SPORTSMEN) {
                        response.addButton(new DevBotButton("help", "/help"));
                        response.addButton(new DevBotButton("feedback", "/feedback"));
                        response.setNewButtonsLine();
                        response.addButton(new DevBotButton("join-club", "/join-club"));
                        response.addButton(new DevBotButton("profile", "/check-profile"));
                        response.setInlineButtons(true);
                    }
                    else {
                        response.addButton(new DevBotButton("help", "/help"));
                        response.addButton(new DevBotButton("feedback", "/feedback"));
                        response.setNewButtonsLine();
                        response.addButton(new DevBotButton("create club", "/create-club"));
                        response.addButton(new DevBotButton("clubs", "/clubs"));
                        response.setInlineButtons(true);
                    }
                }
                else {
                    response.addButton(new DevBotButton("help", "/help"));
                    response.addButton(new DevBotButton("feedback", "/feedback"));
                    response.setNewButtonsLine();
                    response.addButton(new DevBotButton("create club", "/create-club"));
                    response.addButton(new DevBotButton("check location", "/check-location"));
                    response.setInlineButtons(true);
                }
            }

            telegramBot.getResponseSender()
                    .sendDevBotResponse(response);
            responseStatus += "OK";
        }
        return responseStatus;
    }
}
