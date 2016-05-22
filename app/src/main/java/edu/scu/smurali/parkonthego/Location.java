package edu.scu.smurali.parkonthego;

import java.io.Serializable;

/**
 * Created by VARUN RAPARLA on 5/21/2016.
 */
public class Location implements Serializable {
    int id;
    double latitude;
    double longitude;
    double price;
    String address;
    String help;

    public Location(int id, double latitude, double longitude, double price, String address, String help) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.address = address;
        this.help = help;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
}
