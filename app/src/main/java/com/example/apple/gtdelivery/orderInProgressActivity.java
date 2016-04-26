package com.example.apple.gtdelivery;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import at.markushi.ui.CircleButton;

public class orderInProgressActivity extends Activity {
    Firebase firebaseRef;
    Firebase userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in_progress);

        //setting up the text and the fonts
        String message = getIntent().getStringExtra("acceptedOrder");
        final String oUID = getIntent().getStringExtra("ordererUID");

        TextView orderMessage = (TextView) findViewById(R.id.textView3);
        orderMessage.setText(message);
        Typeface normalType = Typeface.createFromAsset(getAssets(), "fonts/YanoneKaffeesatz-Regular.ttf");
        orderMessage.setTypeface(normalType);
        TextView title = (TextView) findViewById(R.id.textView2);
        Typeface comicFont = Typeface.createFromAsset(getAssets(), "fonts/BADABB.ttf");
        title.setTypeface(comicFont);

        //Setup Firebase
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://gtfood.firebaseio.com/");
        userRef = firebaseRef.child("status_table").child(oUID);

        //adding listener for status to change from O to A
        final ChildEventListener aListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String status = dataSnapshot.getValue().toString();
                status.trim();
                System.out.println(dataSnapshot.getValue());
                if (status.equals("D")) {
                    Intent intent = new Intent(orderInProgressActivity.this, dummyDeliveredActivty.class);
                    intent.putExtra("ordererUID", oUID);
                    startActivity(intent);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        userRef.addChildEventListener(aListener);

    }

}
