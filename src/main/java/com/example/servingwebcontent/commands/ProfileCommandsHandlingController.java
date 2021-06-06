package com.example.servingwebcontent.commands;

import com.example.message.data.City;
import com.example.message.data.DevBotButton;
import com.example.message.data.UserActivityStatus;
import com.example.servingwebcontent.DevBotUtils;
import com.example.message.DevBotRequest;
import com.example.message.DevBotResponse;
import com.example.message.data.ExpectedData;
import com.example.servingwebcontent.components.Hierarchy;
import com.example.servingwebcontent.components.TokenStorage;
import com.google.gson.Gson;
import com.google.inject.internal.cglib.reflect.$FastMember;
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

    @RequestMapping(value = "/command/profile/fio", method = RequestMethod.POST)
    @ResponseBody
    public String fioCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /fio form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();


        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.USER_FIO) {
            if (request.getMessage() != null) {
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setFIO(request.getMessage());
                response.setMessage("Your FIO is " + request.getMessage() + "\n" +
                        "Send me your location to identify your city");
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_CITY_LOCATION);
                response.addButton(new DevBotButton("send location", "send location", true));
            }
            else {
                response.setMessage("Please send me your FIO");
                response.setNoButtons(true);
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_FIO);
            }
        } else {
            response.setMessage("NO DATA EXPECTED");
        }

        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }

    @RequestMapping(value = "/command/profile/city-location", method = RequestMethod.POST)
    @ResponseBody
    public String cityLocationCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /cityLocationCommand form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();


        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.USER_CITY_LOCATION) {
            if (request.isHasLocation()) {
                final City city = hierarchy.getCityByLocation(request.getLocation());
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setCity(city);
                response.setMessage("Your City is " + city.getName() + "\n" +
                        "Chose status - administrator or sportsmen");
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_ACTIVITY_STATUS);
                response.addButton(new DevBotButton("administrator", "administrator"));
                response.addButton(new DevBotButton("sportsmen", "sportsmen"));
            }
            else {
                response.setMessage("Please send me your Location to identify yor city");
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_CITY_LOCATION);
                response.addButton(new DevBotButton("send location", "send location", true));
            }
        } else {
            response.setMessage("NO DATA EXPECTED");
        }

        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }

    @RequestMapping(value = "/command/profile/activity-status", method = RequestMethod.POST)
    @ResponseBody
    public String statusCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /status form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();


        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.USER_ACTIVITY_STATUS) {
            if (request.getMessage() != null) {
                if (request.getMessage().equals("administrator")) {
                    response.setMessage("Your status is administrator");
                    hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                            .setActivityStatus(UserActivityStatus.ADMINISTRATOR);
                    hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                            .setExpectedData(ExpectedData.NONE);
                }
                else if (request.getMessage().equals("sportsmen")) {
                    response.setMessage("Your status is sportsmen");
                    hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                            .setActivityStatus(UserActivityStatus.SPORTSMEN);
                    hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                            .setExpectedData(ExpectedData.NONE);
                }
                else {
                    response.setMessage("Please chose status - administrator or sportsmen");
                    hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                            .setExpectedData(ExpectedData.USER_ACTIVITY_STATUS);
                    response.addButton(new DevBotButton("administrator", "administrator"));
                    response.addButton(new DevBotButton("sportsmen", "sportsmen"));
                }
            }
            else {
                response.setMessage("Please chose status - administrator or sportsmen");
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_ACTIVITY_STATUS);
                response.addButton(new DevBotButton("administrator", "administrator"));
                response.addButton(new DevBotButton("sportsmen", "sportsmen"));
            }
        } else {
            response.setMessage("NO DATA EXPECTED");
        }

        response.setUserActivityStatus(hierarchy.getUserToUpdateByTelegramId(request.getUserChatId()).getActivityStatus());
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        return "OK";
    }

    @RequestMapping(value = "/command/profile/join-club", method = RequestMethod.POST)
    @ResponseBody
    public String joinClubCommand(@RequestBody DevBotRequest request) {
        hierarchy.gotNewRequest();
        System.out.println("USER_COMMAND: /status form : " + request.getUserChatId());

        final DevBotResponse response = new DevBotResponse();
        response.setReceiverChatId(request.getUserChatId());
        final Gson jsonConverter = new Gson();

        boolean flag = false;

        final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();
        if (expectedData == ExpectedData.NONE) {
            response.setMessage("Send club code");
            hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                    .setExpectedData(ExpectedData.CLUB_CODE);
        }
        else {
            response.setMessage("You joined to club Football in Saint-Petersburg");
            flag = true;
        }

        response.setUserActivityStatus(hierarchy.getUserToUpdateByTelegramId(request.getUserChatId()).getActivityStatus());
        DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());
        if (flag) {
            try {
                Thread.sleep(10000);
                response.setReceiverChatId(request.getUserChatId());
                response.setMessage("Training starts after 15 minutes. It's time to check your location\n" +
                        "Send me your location");
                DevBotButton button = new DevBotButton("/send-location", "send location");
                button.setRequiredLocation(true);
                response.addButton(button);
                hierarchy.getUserToUpdateByTelegramId(request.getUserChatId())
                        .setExpectedData(ExpectedData.USER_LOCATION);

                response.setUserActivityStatus(hierarchy.getUserToUpdateByTelegramId(request.getUserChatId()).getActivityStatus());
                DevBotUtils.httpsPOSTRequest(TELEGRAM_RESPONSE_CONTROLLER, jsonConverter.toJson(response).getBytes());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "OK";
    }


}
