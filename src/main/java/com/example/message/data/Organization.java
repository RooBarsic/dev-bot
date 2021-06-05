package com.example.message.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Organization {
    private List<Club> clubsList;

    Organization() {
        clubsList = new ArrayList<>();
    }

    public void addClub(@NotNull final Club club) {
        clubsList.add(club);
    }

    public void removeAllClubs() {
        clubsList.clear();
    }

    public List<Club> getClubsList() {
        return new ArrayList<>(clubsList);
    }
}
