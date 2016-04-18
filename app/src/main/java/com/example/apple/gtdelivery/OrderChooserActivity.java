package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
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

        Query orderQuery = statusTableRef.orderByChild("Status").equalTo("O");

        //Setting up current available orders via querying Firebase
        //Current available orders are not sorted at all, ie. the deliverer has to go
        //through every order to find one that is at the same location as them
        //TODO: Implement sorting logic
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

        //Making adapter for Order class using Fundapter
        BindDictionary<Order> dictionary = new BindDictionary<Order>();
        dictionary.addStringField(R.id.ordererName, new StringExtractor<Order>() {
            @Override
            public String getStringValue(Order order, int i) {
                return order.getOrdererName();
            }
        });

        dictionary.addStringField(R.id.ordererRating, new StringExtractor<Order>() {
            @Override
            public String getStringValue(Order order, int i) {
                return order.getOrdererRating() + "";
            }
        });

        dictionary.addStringField(R.id.deliveryFee, new StringExtractor<Order>() {
            @Override
            public String getStringValue(Order order, int i) {
                return "$" + order.getDeliveryFee() + "";
            }
        });

        FunDapter<Order> adapter = new FunDapter<>(getBaseContext(), availableOrders, R.layout.available_orders_list_item, dictionary);

    }

}
