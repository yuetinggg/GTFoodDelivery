package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

public class OrderChooserActivity extends Activity {

    Firebase firebaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_chooser);

        //Setting up Firebase context
        Firebase.setAndroidContext(this);
        firebaseref = new Firebase("https://gtfood.firebaseio.com/");
        Firebase statusTableRef = firebaseref.child("status_table");

        Query orderQuery = firebaseref.orderByChild("Status").equalTo("O");

    }

}
