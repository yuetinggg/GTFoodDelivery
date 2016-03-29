package com.example.apple.gtdelivery;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.firebase.client.Firebase;

import java.util.HashMap;

public class OrderOrDeliver extends Activity {
    orderOrDeliverCircle oodCircle;
    RelativeLayout wholeView;
    Firebase firebaseRef;
    String uid;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_or_deliver);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://gtfood.firebaseio.com/");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
            email = extras.getString("email");
        }
        oodCircle = (orderOrDeliverCircle) findViewById(R.id.oodCircle);
        wholeView = (RelativeLayout) findViewById(R.id.wholeView);
        oodCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Dividing the screen into sides
                float touchX = event.getX();
                float touchY = event.getY();

                if (touchX < wholeView.getMeasuredWidth()/2) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("email", email);
                    map.put("Restaurant", "");
                    map.put("Food Item", "");
                    map.put("Price", 0.0);
                    map.put("Status", "O");
                    HashMap<String, HashMap<String, Object>> mainMap = new HashMap<String, HashMap<String, Object>>();
                    mainMap.put(uid, map);
                    firebaseRef.child("status_table").child(uid).updateChildren(map);
                    toOrder();
                } else {
                    //TODO: Implement start the delivery activity
                    toDeliver();
                }
                return true;
            }
        });

    }

    private void toOrder() {
        Intent i = new Intent(this, FoodChooserActivity.class);
        startActivity(i);
    }

    //TODO: Implement starting the new activity for delivery
    private void toDeliver() {
        Intent i = new Intent(this, FoodChooserActivity.class);
        //startActivity(i);
    }

}
