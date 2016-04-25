package com.example.apple.gtdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OrderSearchActivity extends Activity {
    CircularFillableLoaders loader;
    Button cancelButton;
    Boolean isCancelled = false;
    Boolean isAccepted = false;

    Firebase firebaseRef;
    Firebase userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);

        //Setup Firebase
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://gtfood.firebaseio.com/");
        userRef = firebaseRef.child("status_table").child(firebaseRef.getAuth().getUid());

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
                if (status.equals("A")) {
                    isAccepted = true;
                    Intent intent = new Intent(OrderSearchActivity.this, AddCardActivity.class);
                    startActivity(intent);
                } else {
                    isCancelled = true;
                    Intent intent = new Intent(OrderSearchActivity.this, FoodChooserActivity.class);
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

        //Setting up loading widget
        loader = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);

        //Setting up the cancel button and the onclicklistener
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAccepted) {
                    Toast.makeText(OrderSearchActivity.this, "Order has been accepted. Order cannot be cancelled!", Toast.LENGTH_LONG);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(OrderSearchActivity.this);
                    alert.setTitle("Confirm");
                    String message = "Cancel Order?";
                    alert.setMessage(message).setCancelable(false).
                            setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).
                            setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userRef.removeValue();
                                    Intent intent = new Intent(OrderSearchActivity.this, FoodChooserActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                }
            }
        });
    }

}
