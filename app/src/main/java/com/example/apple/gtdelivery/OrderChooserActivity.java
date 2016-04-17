package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class OrderChooserActivity extends Activity {
    Firebase firebaseref;
    ArrayList<Order> availableOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_chooser);

        //Setting up Firebase context
        Firebase.setAndroidContext(this);
        firebaseref = new Firebase("https://gtfood.firebaseio.com/");
        Firebase statusTableRef = firebaseref.child("status_table");

        Query orderQuery = firebaseref.orderByChild("Status").equalTo("O");

        //Setting up current available orders via querying Firebase
        availableOrders = new ArrayList<>();
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    availableOrders.add(order);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Realtime checking if any of the available orders have become unavailable
        orderQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        })

    }

}
