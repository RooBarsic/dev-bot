package com.example.message.data;

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

}
