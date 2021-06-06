package com.example.message.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Club {
    private String clubName;
    private Location location;
    private String clubCode = "";
    private List<DevBotUser> participants;
    private List<Event> eventsList;
    private DevBotUser admin;

    public Club() {
        clubName = "none";
        location = new Location();
    }

    public void addParticipant(@NotNull final DevBotUser user) {
        participants.add(user.copyCurUser());
    }

    public void addEvent(@NotNull final Event event) {
        eventsList.add(event);
    }

    public List<DevBotUser> getUsers() {
        return new ArrayList<>(participants);
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getClubCode() {
        return clubCode;
    }

    public void setClubCode(String clubCode) {
        this.clubCode = clubCode;
    }

    public List<DevBotUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<DevBotUser> participants) {
        this.participants = participants;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public DevBotUser getAdmin() {
        return admin;
    }

    public void setAdmin(DevBotUser admin) {
        this.admin = admin;
    }
}
