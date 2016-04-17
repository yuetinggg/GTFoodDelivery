package com.example.apple.gtdelivery;

import java.util.ArrayList;

/**
 * Created by yuetinggg on 4/16/16.
 */
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

    public Order(double deliveryFee, ArrayList<String> foodItems, String deliveryLocation, String restaurant, String status, double total, String email, String ordererName, int ordererRating) {
        this.deliveryFee = deliveryFee;
        this.foodItems = foodItems;
        this.deliveryLocation = deliveryLocation;
        this.restaurant = restaurant;
        this.status = status;
        this.total = total;
        this.email = email;
        this.ordererName = ordererName;
        this.ordererRating = ordererRating;
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
}
