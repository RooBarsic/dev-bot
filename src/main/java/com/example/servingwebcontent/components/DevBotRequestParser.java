package com.example.servingwebcontent.components;

import com.example.servingwebcontent.DevBotUtils;
import com.example.message.DevBotRequest;
import com.example.message.data.ExpectedData;
import com.google.gson.Gson;
import com.google.inject.internal.cglib.reflect.$FastMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class DevBotRequestParser {
    private final String START_COMMAND_REST_CONTROLLER;
    private final String HELP_COMMAND_REST_CONTROLLER;
    private final String FEEDBACK_COMMAND_REST_CONTROLLER;
    private final String EMPTY_COMMAND_CONTROLLER;
    private final String CLUB_CREATE_COMMAND_CONTROLLER;
    private final String CHECK_LOCATION_COMMAND_CONTROLLER;
    private final String PROFILE_FIO_COMMAND_CONTROLLER;
    private final String PROFILE_CITY_LOCATION_COMMAND_CONTROLLER;
    private final String PROFILE_ACTIVITY_STATUS_COMMAND_CONTROLLER;
    private  String JOIN_CLUB;
    private final ConcurrentLinkedDeque<DevBotRequest> receivedRequestsQueue;
    private final Hierarchy hierarchy;

    @Autowired
    DevBotRequestParser(TokenStorage tokenStorage, Hierarchy hierarchy) {
        final String appUrl = tokenStorage.getTokens("APP_HEROKU_URL");
        START_COMMAND_REST_CONTROLLER = appUrl + "/command/start";
        HELP_COMMAND_REST_CONTROLLER = appUrl + "/command/help";
        EMPTY_COMMAND_CONTROLLER = appUrl + "/command/empty";
        FEEDBACK_COMMAND_REST_CONTROLLER = appUrl + "/command/profile/feedback";
        CLUB_CREATE_COMMAND_CONTROLLER = appUrl + "/club/create";
        CHECK_LOCATION_COMMAND_CONTROLLER = appUrl + "/club/check-position";
        PROFILE_FIO_COMMAND_CONTROLLER = appUrl + "/command/profile/fio";
        PROFILE_CITY_LOCATION_COMMAND_CONTROLLER = appUrl + "/command/profile/city-location";
        PROFILE_ACTIVITY_STATUS_COMMAND_CONTROLLER = appUrl + "/command/profile/activity-status";
        JOIN_CLUB = appUrl + "/command/profile/join-club";

        receivedRequestsQueue = new ConcurrentLinkedDeque<>();
        this.hierarchy = hierarchy;

        startRequestsProcessing();
    }

    /**
     * Process and redirect incoming request to required command REST controllers
     */
    public void startRequestsProcessing() {
        new Thread(() -> {
            final Gson jsonConverter = new Gson();
            while (true) {
                while (receivedRequestsQueue.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //process while has request
                while (!receivedRequestsQueue.isEmpty()) {
                    final DevBotRequest request = receivedRequestsQueue.getFirst();
                    receivedRequestsQueue.removeFirst();

                    //find controller url for requested command
                    final String commandRestController;
                    final ExpectedData expectedData = hierarchy.getOrCreateUserByTelegramId(request.getUserChatId()).getExpectedData();

//                    if (request.getMessage() != null && request.getMessage().equals("ignore")) {
//                        continue;
//                    }

                    if (request.getMessage() == null) {
                        switch (expectedData) {
                            case FEEDBACK:
                                commandRestController = FEEDBACK_COMMAND_REST_CONTROLLER;
                                break;
                            case CLUB_NAME:
                                commandRestController = CLUB_CREATE_COMMAND_CONTROLLER;
                                break;
                            case CLUB_LOCATION:
                                commandRestController = CLUB_CREATE_COMMAND_CONTROLLER;
                                break;
                            case USER_LOCATION:
                                commandRestController = CHECK_LOCATION_COMMAND_CONTROLLER;
                                break;
                            case USER_FIO:
                                commandRestController = PROFILE_FIO_COMMAND_CONTROLLER;
                                break;
                            case USER_CITY_LOCATION:
                                commandRestController = PROFILE_CITY_LOCATION_COMMAND_CONTROLLER;
                                break;
                            case USER_ACTIVITY_STATUS:
                                commandRestController = PROFILE_ACTIVITY_STATUS_COMMAND_CONTROLLER;
                                break;
                            case CLUB_CODE:
                                commandRestController = JOIN_CLUB;
                                break;
                            default:
                                commandRestController = EMPTY_COMMAND_CONTROLLER;
                        }
                    }
                    else if (request.getMessage().startsWith("/start")) {
                        commandRestController = START_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/help")) {
                        commandRestController = HELP_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/feedback")) {
                        commandRestController = FEEDBACK_COMMAND_REST_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/create-club")) {
                        commandRestController = CLUB_CREATE_COMMAND_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/check-location")) {
                        commandRestController = CHECK_LOCATION_COMMAND_CONTROLLER;
                    }
                    else if (request.getMessage().startsWith("/join-club")) {
                        commandRestController = JOIN_CLUB;
                    }
                    else {
                        switch (expectedData) {
                            case FEEDBACK:
                                commandRestController = FEEDBACK_COMMAND_REST_CONTROLLER;
                                break;
                            case CLUB_NAME:
                                commandRestController = CLUB_CREATE_COMMAND_CONTROLLER;
                                break;
                            case CLUB_LOCATION:
                                commandRestController = CLUB_CREATE_COMMAND_CONTROLLER;
                                break;
                            case USER_LOCATION:
                                commandRestController = CHECK_LOCATION_COMMAND_CONTROLLER;
                                break;
                            case USER_FIO:
                                commandRestController = PROFILE_FIO_COMMAND_CONTROLLER;
                                break;
                            case USER_CITY_LOCATION:
                                commandRestController = PROFILE_CITY_LOCATION_COMMAND_CONTROLLER;
                                break;
                            case USER_ACTIVITY_STATUS:
                                commandRestController = PROFILE_ACTIVITY_STATUS_COMMAND_CONTROLLER;
                                break;
                            case CLUB_CODE:
                                commandRestController = JOIN_CLUB;
                                break;
                            default:
                                commandRestController = EMPTY_COMMAND_CONTROLLER;
                        }
                    }

                    DevBotUtils.httpsPOSTRequest(commandRestController, jsonConverter.toJson(request).getBytes());
                }
            }
        }).start();
    }

    public ConcurrentLinkedDeque<DevBotRequest> getReceivedRequestsQueue() {
        return receivedRequestsQueue;
    }
}
