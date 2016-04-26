package com.example.apple.gtdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import butterknife.Bind;
import butterknife.ButterKnife;

public class foundWaitingActivity extends Activity {
    @Bind(R.id.verifyDeliveryButton) Button verified;
    @Bind(R.id.foundWaiting) CircularFillableLoaders loader;
    String toRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_waiting);
        ButterKnife.bind(this);

        //Setting up loading widget
        loader = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);

        //Setting up firebase
        Firebase.setAndroidContext(this);
        Firebase firebaseref = new Firebase(Constants.BASE_URL);
        final Firebase orderRef = firebaseref.child("status_table").child(firebaseref.getAuth().getUid());

        //getting the to be rated user's rating and number of people that rated them and name
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toRate = (String) dataSnapshot.child("delivererUID").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRef.child("status").setValue("D");
                Intent i = new Intent(foundWaitingActivity.this, ratingUserActivity.class);
                i.putExtra("UID", toRate);
                startActivity(i);
            }
        });


    }

}
