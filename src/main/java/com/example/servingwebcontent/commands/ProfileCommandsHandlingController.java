package com.example.servingwebcontent.commands;

import com.example.servingwebcontent.DevBotUtils;
import com.example.message.DevBotRequest;
import com.example.message.DevBotResponse;
import com.example.message.data.ExpectedData;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileCommandsHandlingController{
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;
    private final String TELEGRAM_FEEDBACK_GROUP_ID;

    @Autowired
    ProfileCommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
        TELEGRAM_FEEDBACK_GROUP_ID = tokenStorage.getTokens("TELEGRAM_FEEDBACK_GROUP_ID");
    }

    @RequestMapping(value = "/command/profile/feedback", method = RequestMethod.POST)
    @ResponseBody
    public String feedbackCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /feedback form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();


        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.FEEDBACK) {
            response.setMessage("Thank you for your feedback.\nYour feedback was received.\nWe will revue it soon.");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.NONE);

            //TODO send feedback to admins
            final DevBotResponse feedback = new DevBotResponse();
            feedback.setReceiverChatId(TELEGRAM_FEEDBACK_GROUP_ID);
            feedback.setMessage("#feedback\nFrom " + request.getUserChatId() + ":\n" + request.getMessage());
            DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(feedback).getBytes());
        } else {
            response.setMessage("We appreciate your feedback.\nPlease send your feedback.");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.FEEDBACK);
        }

        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }
}
