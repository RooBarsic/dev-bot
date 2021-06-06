package com.example.message.data;

public class City {
    private Location location = new Location();
    private String name = "";

    public City(String name, Location location) {
        this.location = location;
        this.name = name;
    }

    public City() {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
