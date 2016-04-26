package com.example.apple.gtdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import at.markushi.ui.CircleButton;

public class dummyDeliveredActivty extends Activity {
    CircularFillableLoaders loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_delivered_activty);

        //Setting up loading widget
        loader = (CircularFillableLoaders) findViewById(R.id.lalala);

        final String oUID = getIntent().getStringExtra("ordererUID");

        Button done = (Button) findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dummyDeliveredActivty.this, ratingUserActivity.class);
                intent.putExtra("UID", oUID);
                startActivity(intent);
            }
        });
    }

}
