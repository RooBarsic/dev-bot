package com.example.message.data;

import org.jetbrains.annotations.NotNull;

public class Location {
    private float longitude;
    private float latitude;

    public Location() {
        longitude = (float) 0.0;
        latitude = (float) 0.0;
    }

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double countDist(@NotNull final Location location) {
        double dist = ((this.latitude - location.latitude) * (this.latitude - location.latitude));
        dist += ((this.longitude - location.longitude) * (this.longitude - location.longitude));
        System.out.println("dist = " + dist);
        return Math.floor(dist);
    }
}
