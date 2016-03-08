package com.example.apple.gtdelivery;

/**
 * Created by yuetinggg on 3/8/16.
 */
public class menuItem {
    private String rName;
    private String name;
    private String price;

    public menuItem(String rName, String name, String price) {
        this.rName = rName;
        this.name = name;
        this.price = price;
    }

    public String getrName() {
        return rName;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
