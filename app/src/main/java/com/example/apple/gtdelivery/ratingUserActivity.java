package com.example.apple.gtdelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ratingUserActivity extends Activity {
    Firebase firebaseref;
    int numRatings;
    double currentRating;
    String toBeRatedName;

    @Bind(R.id.ratingBar) RatingBar rating;
    @Bind(R.id.tobeRated) TextView toBeRated;
    @Bind(R.id.textView4) TextView orderComplete;
    @Bind(R.id.textView5) TextView pleaseRate;
    @Bind(R.id.doneRated) CircleButton done;

    String toRateUID;
    String TAG = "RatingUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_user);
        ButterKnife.bind(this);

        //getUID of this user, that's rating the other user
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = prefs.edit();
        final String currentUID = prefs.getString("uid", "");

        //get to be rated;
        toRateUID = getIntent().getStringExtra("UID");
        Log.d(TAG, "TO RATE UID: " + toRateUID);
        //setting up database connections
        Firebase.setAndroidContext(this);
        firebaseref = new Firebase(Constants.BASE_URL);
        final Firebase statusTableRef = firebaseref.child("status_table");
        final Firebase usersRef = firebaseref.child("users").child(toRateUID);


        //getting the to be rated user's rating and number of people that rated them and name
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "NUM_RATINGS: " + dataSnapshot.child(Constants.USER_NUM_RATINGS).getValue());
                numRatings = Integer.parseInt(dataSnapshot.child(Constants.USER_NUM_RATINGS).getValue() + "");
                currentRating = Double.parseDouble(dataSnapshot.child(Constants.USER_RATING).getValue() + "");
                toBeRatedName = (String) dataSnapshot.child(Constants.USER_NAME).getValue();
                //set fonts
                Typeface normalType = Typeface.createFromAsset(getAssets(), Constants.NORMAL_FONT);
                Typeface comicFont = Typeface.createFromAsset(getAssets(), Constants.COMIC_FONT);
                toBeRated.setTypeface(comicFont);
                toBeRated.setText(toBeRatedName);
                orderComplete.setTypeface(comicFont);
                pleaseRate.setTypeface(normalType);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //setting up the button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double setRating = rating.getRating();
                System.out.println(setRating);
                double newRating = (((currentRating) * numRatings) + setRating)/(numRatings + 1);
                usersRef.child("num_ratings").setValue(numRatings+1);
                System.out.println(newRating);
                usersRef.child("rating").setValue(newRating);
                firebaseref.child("users").child(currentUID).child("status").setValue(Constants.NOTHING);

                edit.putString("Status", Constants.NOTHING);
                edit.commit();

                //Go to the main page
                Intent i = new Intent(ratingUserActivity.this, OrderOrDeliver.class);
                startActivity(i);

            }
        });



    }

}
