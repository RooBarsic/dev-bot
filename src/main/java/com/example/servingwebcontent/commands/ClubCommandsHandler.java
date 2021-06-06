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
            response.setInlineButtons(true);
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
            double dist = hierarchy.club.getLocation().calcDistAzim(request.getLocation());
            double dist_cosin = hierarchy.club.getLocation().calcDistMavenCosines(request.getLocation());
            double dist_haversine = hierarchy.club.getLocation().calcDistMavenHaversine(request.getLocation());
            double dist_Vincenty = hierarchy.club.getLocation().calcDistMavenVincenty(request.getLocation());
            double distance = hierarchy.club.getLocation().distance(request.getLocation());


            String message = "";
            message += "calcDistMavenCosines = " + dist_cosin + "\n" +
                    "calcDistMavenHaversine = " + dist_haversine  + "\n" +
                    "calcDistMavenVincenty = " + dist_Vincenty + "\n" +
                    "calcDistAzim = " + dist + "\n" +
                    "distance = " + distance  + "\n";

            if (dist > 10.0) {
                message += "You are too fare from club. Please come to club and try again today!\n";
            }
            else {
                message += "You are in the club. You got +1 point to your bonuses. Congratulations.\n";
            }
            message += "\nYour distance from club location = " + Double.toString(dist) + " meters\n" +
                    "Club latitude : " + hierarchy.club.getLocation().getLatitude() + "\n" +
                    "Club longitude : " + hierarchy.club.getLocation().getLongitude() + "\n\n" +
                    "Your latitude : " + request.getLocation().getLatitude() + "\n" +
                    "Your longitude : " + request.getLocation().getLongitude();
            response.setMessage(message);
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.NONE);
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
