package com.example.apple.gtdelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stripe.android.model.Card;

/**
 * Created by yuetinggg on 4/16/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class localUser {
    private String email;
    private String name;
    private int rating;
    private String status;
    private String uid;
    private int num_ratings;
    public static final String MY_PREFS_NAME = "UserPrefs";
    private String customer_stripe_id;
    SharedPreferences pref;

    public localUser() {}

    @JsonIgnore
    public localUser(String email, String name, int rating, String status, String uid, Context context, String customer_stripe_id) {
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.status = status;
        this.uid = uid;
        this.customer_stripe_id = customer_stripe_id;
        pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
    }

    @JsonIgnore
    public localUser(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @JsonIgnore
    public void logUserIn() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("loggedIn", true);
        edit.putString("Email", email);
        edit.putString("Name", name);
        edit.putInt("Rating", rating);
        edit.putString("Status", status);
        edit.putString("uid", uid);
        edit.putInt("num_ratings", num_ratings);
        edit.putString("customer_stripe_id", customer_stripe_id);
        System.out.println(customer_stripe_id + "1");
        edit.commit();
        System.out.println(pref.getString("customer_stripe_id", "") + "SHIT");
    }

    @JsonIgnore
    public boolean isLoggedIn() {
        return pref.getBoolean("loggedIn", false);
    }

    @JsonIgnore
    public boolean logOut() {
        if (!isLoggedIn()) {
            return false;
        } else {
            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.commit();
            return true;
        }
    }


    public String getEmail() {
        return (email == null ? pref.getString("Email", ""):email);
    }

    public String getName() {
        return (name == null ? pref.getString("Name", ""):email);
    }

    public int getRating() {
        return (rating == 0 ? pref.getInt("Rating", 0):rating);
    }

    public String getStatus() {
        return (status == null ? pref.getString("Status", ""):email);
    }

    public String getUid() {
        return (uid == null ? pref.getString("uid", ""):uid);
    }

    public int getNum_ratings() {
        return num_ratings;
    }

    public String getCustomer_stripe_id() {
        return customer_stripe_id;
    }

    @JsonIgnore
    public void setPref(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
