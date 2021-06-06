package com.example.message.data;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
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

    public double dist3(@NotNull final Location location) {
        double lon = this.longitude,
                lat = this.latitude,
                lon2 = location.longitude,
                lat2 = location.latitude;
        double result = 111.2 * Math.sqrt( (lon - lon2)*(lon - lon2) + (lat - lat2)*Math.cos(Math.PI*lon/180)*(lat - lat2)*Math.cos(Math.PI*lon/180));
        return result;
    }

    public double distance(@NotNull final Location location) {
        double lat1 = location.latitude;
        double lon1 = location.longitude;
        double lat2 = this.latitude;
        double lon2 = this.longitude;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 1.609344 * 1000;
        return (dist) * 100; // meters
    }
    /* The function to convert decimal into radians */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    /* The function to convert radians into decimal */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public double calcDistAzim(@NotNull final Location location) {
        double R = 63710320.0;
        double z1 = R * Math.cos(this.latitude);
        double x1 = R * Math.sin(this.latitude) * Math.cos(this.longitude);
        double y1 = R * Math.sin(this.latitude) * Math.sin(this.longitude);

        double z2 = R * Math.cos(location.latitude);
        double x2 = R * Math.sin(location.latitude) * Math.cos(location.longitude);
        double y2 = R * Math.sin(location.latitude) * Math.sin(location.longitude);

        double s = ((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)) + ((z1 - z2) * (z1 - z2));
        s = Math.sqrt(s);

        return s;
    }

    /** Spherical law of cosines */
    public double calcDistMavenCosines(@NotNull final Location location) {
        //Kew, London
        Coordinate lat = Coordinate.fromDegrees(this.latitude); //51.4843774);
        Coordinate lng = Coordinate.fromDegrees(this.longitude); //-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond, London
        lat = Coordinate.fromDegrees(location.latitude); //51.4613418);
        lng = Coordinate.fromDegrees(location.longitude); //-0.3035466);
        Point richmond = Point.at(lat, lng);

        double distance = EarthCalc.gcd.distance(richmond, kew); //in meters

        return distance;
    }

    /** Haversine formula */
    public double calcDistMavenHaversine(@NotNull final Location location) {
        //Kew, London
        Coordinate lat = Coordinate.fromDegrees(this.latitude); //51.4843774);
        Coordinate lng = Coordinate.fromDegrees(this.longitude); //-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond, London
        lat = Coordinate.fromDegrees(location.latitude); //51.4613418);
        lng = Coordinate.fromDegrees(location.longitude); //-0.3035466);
        Point richmond = Point.at(lat, lng);

        double distance = EarthCalc.haversine.distance(richmond, kew); //in meters
        return distance;
    }

    /** Vincenty formula */
    public double calcDistMavenVincenty(@NotNull final Location location) {
        //Kew, London
        Coordinate lat = Coordinate.fromDegrees(this.latitude); //51.4843774);
        Coordinate lng = Coordinate.fromDegrees(this.longitude); //-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond, London
        lat = Coordinate.fromDegrees(location.latitude); //51.4613418);
        lng = Coordinate.fromDegrees(location.longitude); //-0.3035466);
        Point richmond = Point.at(lat, lng);

        double distance = EarthCalc.vincenty.distance(richmond, kew); //in meters
        return distance;
    }


}
