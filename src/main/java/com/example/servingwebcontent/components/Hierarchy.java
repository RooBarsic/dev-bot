package com.example.servingwebcontent.components;


import com.example.message.data.Club;
import com.example.message.data.DevBotUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Hierarchy {
    public Club club = new Club();
    private List<DevBotUser> usersList;
    private AtomicInteger requestsNumber = new AtomicInteger(0);

    Hierarchy() {
        usersList = new LinkedList<>();
    }

    public void updateHierarchy(final Hierarchy hierarchy) {
        for (final DevBotUser user : hierarchy.usersList) {
            if (containsThatTelegramId(user.getTelegramId())) {
                DevBotUser curUser = getUserToUpdateByTelegramId(user.getTelegramId());
            } else {
                usersList.add(user.copyCurUser());
            }
        }
    }

    public DevBotUser getUserByTelegramId(final String userTelegramId) {
        for (DevBotUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return user.copyCurUser();
            }
        }
        return null;
    }

    public Hierarchy createUserByTelegramId(final String userTelegramId) {
        final DevBotUser DevBotUser = new DevBotUser();
        DevBotUser.setTelegramId(userTelegramId);
        usersList.add(DevBotUser);
        return this;
    }

    public DevBotUser getUserToUpdateByTelegramId(final String userTelegramId) {
        for (DevBotUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return user;
            }
        }
        return new DevBotUser();
    }

    public boolean containsThatTelegramId(final String userTelegramId) {
        for (DevBotUser user : usersList) {
            if (user.getTelegramId().equals(userTelegramId)) {
                return true;
            }
        }
        return false;
    }

    public DevBotUser getOrCreateUserByTelegramId(@NotNull final String targetTelegramId) {
        for (final DevBotUser user : usersList) {
            if (user.getTelegramId().equals(targetTelegramId)) {
                return user;
            }
        }
        DevBotUser newUser = new DevBotUser();
        newUser.setTelegramId(targetTelegramId);
        usersList.add(newUser);
        return newUser;
    }

    public List<DevBotUser> getAllUsers() {
        return new ArrayList<>(usersList);
    }

    public int getRequestsNumber() {
        return requestsNumber.get();
    }

    public void resetRequestNum() {
        requestsNumber.set(0);
    }

    public void gotNewRequest() {
        requestsNumber.incrementAndGet();
    }
}
