package com.example.servingwebcontent.commands;

import com.example.message.DevBotRequest;
import com.example.message.DevBotResponse;
import com.example.message.data.Club;
import com.example.message.data.DevBotButton;
import com.example.message.data.ExpectedData;
import com.example.servingwebcontent.DevBotUtils;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClubCommandsHandler {
    @Autowired
    private Hierarchy hierarchy;
    private final String TELEGRAM_RESPONSE_CONTROLLER;

    @Autowired
    ClubCommandsHandler(TokenStorage tokenStorage) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        TELEGRAM_RESPONSE_CONTROLLER = appUrl + "/send/telegram";
    }

    @RequestMapping(value = "/club/create", method = RequestMethod.POST)
    @ResponseBody
    public String createOrganization(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /start form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        response.setMessage("Ayyyy");

        ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.NONE) {
            response.setMessage("Send me club name");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.CLUB_NAME);
        }
        else if (expectedData == ExpectedData.CLUB_NAME) {
            hierarchy.club.setClubName(request.getMessage());
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.NONE);
            response.setMessage("Send club location");
            DevBotButton button = new DevBotButton("send location", "/send-location");
            button.setRequiredLocation(true);
            response.setInlineButtons(false);
            response.addButton(button);
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.CLUB_LOCATION);
        }
        else if (expectedData == ExpectedData.CLUB_LOCATION) {
            if (request.isHasLocation()) {
                hierarchy.club.setLocation(request.getLocation());
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.NONE);
                response.setMessage("Cool. Club name : " + hierarchy.club.getClubName() + "\n" +
                        "Location latitude : " + hierarchy.club.getLocation().getLatitude() + "\n" +
                        "Location longitude : " + hierarchy.club.getLocation().getLongitude());
            }
            else {
                response.setMessage("Send your location");
                DevBotButton button = new DevBotButton("/send-location", "send location");
                button.setRequiredLocation(true);
                response.addButton(button);
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.CLUB_LOCATION);
            }
        }

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }


    @RequestMapping(value = "/club/check-position", method = RequestMethod.POST)
    @ResponseBody
    public String checkPosition(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("GLOBAL_COMMAND: /start form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());

        if (request.isHasLocation()) {
            double dist = hierarchy.club.getLocation().distance(request.getLocation());
            response.setMessage("Distance from club = " + Double.toString(dist) + "\n" +
                    "Club latitude : " + hierarchy.club.getLocation().getLatitude() + "\n" +
                    "Club longitude : " + hierarchy.club.getLocation().getLongitude() + "\n\n" +
                    "Your latitude : " + request.getLocation().getLatitude() + "\n" +
                    "Your longitude : " + request.getLocation().getLongitude());
        }
        else {
            response.setMessage("Send me your location");
            DevBotButton button = new DevBotButton("/send-location", "send location");
            button.setRequiredLocation(true);
            response.addButton(button);
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.USER_LOCATION);
        }

        final Gson jsonConverter = new Gson();
        System.out.println("TELEGRAM_RESPONSE_CONTROLLER = " + TELEGRAM_RESPONSE_CONTROLLER);
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

        return "OK";
    }

}
