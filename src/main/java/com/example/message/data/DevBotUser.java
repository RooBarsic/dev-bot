package com.example.message.data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * about user
 * User : DevBot_id, telegram_id, telegram_login
 *     words : [question, ans, number_of_repeats, next_asking_day], ......
 */
public class DevBotUser {
    private int DevBotId = 0;
    private String telegramId = "";
    private String telegramLogin = "";
    private String FIO = "";
    private City city;
    private UserActivityStatus activityStatus;
    private Club editingClub = new Club();
    private AtomicInteger activityPoints = new AtomicInteger(0);
    private AtomicInteger activityPointsStack = new AtomicInteger(0);

    private ExpectedData expectedData = ExpectedData.NONE;

    public DevBotUser() {
    }

    public DevBotUser copyCurUser() {
        final DevBotUser copy = new DevBotUser();
        copy.setDevBotId(DevBotId);
        copy.setTelegramId(telegramId);
        copy.setTelegramLogin(telegramLogin);
        copy.setExpectedData(expectedData);

        return copy;
    }

    public ExpectedData getExpectedData() {
        return expectedData;
    }

    public DevBotUser setExpectedData(ExpectedData expectedData) {
        this.expectedData = expectedData;
        return this;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public DevBotUser setTelegramId(String telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getTelegramLogin() {
        return telegramLogin;
    }

    public DevBotUser setTelegramLogin(String telegramLogin) {
        this.telegramLogin = telegramLogin;
        return this;
    }

    public int getDevBotId() {
        return DevBotId;
    }

    public void setDevBotId(int DevBotId) {
        this.DevBotId = DevBotId;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public UserActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(UserActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Club getEditingClub() {
        return editingClub;
    }

    public void setEditingClub(Club editingClub) {
        this.editingClub = editingClub;
    }

    public void visitedNewSportActivity() {
        int newPoints = activityPoints.get() + activityPointsStack.get();
        activityPoints.set(newPoints);
        activityPointsStack.incrementAndGet();
    }

    public int getActivityPoints() {
        return activityPoints.get();
    }
}
