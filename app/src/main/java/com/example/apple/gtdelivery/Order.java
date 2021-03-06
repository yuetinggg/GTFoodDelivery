package com.example.apple.gtdelivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by yuetinggg on 4/16/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private double deliveryFee;
    private ArrayList<String> foodItems;
    private String deliveryLocation;
    private String restaurant;
    private String status;
    private double total;
    private String email;
    private String ordererName;
    private int ordererRating;
    private String uid;
    private double ourFee;
    private String delivererUID;

    //Needed for JSON deserialization when retrieving from Firebase
    public Order() {}

    @JsonIgnore
    public Order(double deliveryFee, ArrayList<String> foodItems, String deliveryLocation, String restaurant, String status, double total, String email, String ordererName, int ordererRating, double ourFee) {
        this.deliveryFee = deliveryFee;
        this.foodItems = foodItems;
        this.deliveryLocation = deliveryLocation;
        this.restaurant = restaurant;
        this.status = status;
        this.total = total;
        this.email = email;
        this.ordererName = ordererName;
        this.ordererRating = ordererRating;
        this.ourFee = ourFee;
    }

    public String getOrdererName() {
        return ordererName;
    }

    public int getOrdererRating() {
        return ordererRating;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getTotal() {
        return total;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public ArrayList<String> getFoodItems() {
        return foodItems;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() { return status; }

    @JsonIgnore
    public void setUid(String string) {
        uid = string;
    }

    @JsonIgnore
    public String getUid() {
        return uid;
    }

    public double getOurFee () {return ourFee;}

    public String getDelivererUID() {
        return delivererUID;
    }
}
