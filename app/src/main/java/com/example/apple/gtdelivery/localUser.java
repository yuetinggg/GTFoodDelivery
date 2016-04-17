package com.example.apple.gtdelivery;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yuetinggg on 4/16/16.
 */
public class localUser {
    private String email;
    private String name;
    private int rating;
    private String status;
    private String uid;
    public static final String MY_PREFS_NAME = "UserPrefs";
    SharedPreferences pref;


    public localUser(String email, String name, int rating, String status, String uid, Context context) {
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.status = status;
        this.uid = uid;
        pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
    }

    public localUser(Context context) {
        pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
    }

    public void logUserIn(localUser user) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("loggedIn", true);
        edit.putString("Email", user.getEmail());
        edit.putString("Name", user.getName());
        edit.putInt("Rating", user.getRating());
        edit.putString("Status", user.getStatus());
        edit.putString("uid", user.getUid());
        edit.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean("loggedIn", false);
    }

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
}
