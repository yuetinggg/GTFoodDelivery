package com.example.apple.gtdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import com.stripe.android.*;
import com.stripe.exception.AuthenticationException;

public class Delivering extends Activity {
    // You will need to use your live API key even while testing
    public static final String PUBLISHABLE_KEY = "pk_test_0KPlH4Hw2FUzR7bakDWXZqr1";
    Stripe stripe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering);
        try {
            stripe = new Stripe(PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

    }

}


