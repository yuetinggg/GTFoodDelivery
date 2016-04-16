package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;

import com.firebase.client.Firebase;

public class OrderChooserActivity extends Activity {

    Firebase firebaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_chooser);
    }

}
