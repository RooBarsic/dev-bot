package com.example.servingwebcontent.components;


import com.example.message.data.City;
import com.example.message.data.Club;
import com.example.message.data.DevBotUser;
import com.example.message.data.Location;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class Hierarchy {
    public Club club = new Club();
    private List<DevBotUser> usersList;
    private List<City> registeredCities;
    private List<Club> registeredClubs;

    Hierarchy() {
        usersList = new LinkedList<>();
        registeredCities = new ArrayList<>();
        registeredClubs = new ArrayList<>();

        setDefaultData();
    }

    private void setDefaultData() {
        registeredCities.add(new City("Saint-Petersburg", new Location()));
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

    public City getCityByLocation(Location location) {
        double closestDistance = -1.;
        City nearestCity = null;
        for (City city : registeredCities) {
            double dist = city.getLocation().calcDistMavenCosines(location);
            if (nearestCity == null || dist < closestDistance) {
                closestDistance = dist;
                nearestCity = city;
            }
        }
        return  nearestCity;
    }

    public void gotNewRequest() {
    }

    public void addClub(Club club) {
        registeredClubs.add(club);
    }

    public List<Club> getAllUSerClubs(@NotNull final DevBotUser user) {
        List<Club> clubs = new ArrayList<>();
        for (Club club : registeredClubs) {
            if (club.getAdmin().getTelegramId().equals(user.getTelegramId())) {
                clubs.add(club);
            }
        }
        return clubs;
    }
}
