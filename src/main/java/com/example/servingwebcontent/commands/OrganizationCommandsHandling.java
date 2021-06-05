package com.example.servingwebcontent.commands;

import com.example.message.DevBotRequest;
import com.example.message.DevBotResponse;
import com.example.message.data.ExpectedData;
import com.example.servingwebcontent.DevBotUtils;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrganizationCommandsHandling {
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;

    @Autowired
    OrganizationCommandsHandling(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
    }

    @RequestMapping(value = "/organization/create", method = RequestMethod.POST)
    @ResponseBody
    public String createOrganization(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /start form : " + request.getUserChatId());


        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("Salam. My name is DevSport_bot, I will help you to manage your sport activities");

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

}
