package com.example.message.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Club {
    private String clubName;
    private Location location;
    private List<DevBotUser> participants;
    private List<Event> eventsList;

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
}
