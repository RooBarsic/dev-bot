package com.example.servingwebcontent.commands;


import com.example.message.data.DevBotButton;
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
public class CommandsHandlingController {
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;

    @Autowired
    CommandsHandlingController(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
    }

    @RequestMapping(value = "/command/start", method = RequestMethod.POST)
    @ResponseBody
    public String startCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /start form : " + request.getUserChatId());

        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());
        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.NONE);

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("Salam. I'm DevSport_bot. I will help you with your sport activities.\n" +
                "Please send me your FIO");

        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.USER_FIO);

        response.setNoButtons(true);

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/help", method = RequestMethod.POST)
    @ResponseBody
    public String helpCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /help form : " + request.getUserChatId());

        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());
        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.NONE);

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());

        response.setMessage("/start - it's start command\n" +
                "/help - use to get some help info\n" +
                "/feedback - use to write feedback");

        final Gson jsonConverter = new Gson();
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

    @RequestMapping(value = "/command/empty", method = RequestMethod.POST)
    @ResponseBody
    public String emptyCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: empty command form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("No data expected :(");

        hierarchy.getOrCreateUserByTelegramId(request.getUserChatId());
        hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                .setExpectedData(ExpectedData.NONE);

        final Gson jsonConverter = new Gson();
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }


}
