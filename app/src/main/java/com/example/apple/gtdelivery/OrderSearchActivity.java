package com.example.apple.gtdelivery;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.squareup.picasso.Picasso;

public class OrderSearchActivity extends Activity {
    CircularFillableLoaders loader;
    Button cancelButton;

    Firebase firebaseRef = new Firebase("https://gtfood.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);
        Firebase.setAndroidContext(this);
        //Setting up loading widget
        loader = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);

        //Setting up the cancel button and the onclicklistener
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase userRef = firebaseRef.child("status_table").child(firebaseRef.getAuth().getUid());
                userRef.removeValue();
            }
        });
    }

}
