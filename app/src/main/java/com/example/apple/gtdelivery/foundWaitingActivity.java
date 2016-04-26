package com.example.apple.gtdelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Transfer;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class foundWaitingActivity extends Activity {
    @Bind(R.id.verifyDeliveryButton) Button verified;
    @Bind(R.id.foundWaiting) CircularFillableLoaders loader;
    String toRate;
    String delivererCustID;
    private String TAG = "foundWaitingActivity";
    Double toPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_waiting);
        ButterKnife.bind(this);

        //Setting up loading widget
        loader = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);

        //Setting up firebase
        Firebase.setAndroidContext(this);
        final Firebase firebaseref = new Firebase(Constants.BASE_URL);
        final Firebase orderRef = firebaseref.child("status_table").child(firebaseref.getAuth().getUid());

        //getting the to be rated user's rating and number of people that rated them and name
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toRate = (String) dataSnapshot.child("delivererUID").getValue();
                Double total =(Double) dataSnapshot.child("total").getValue();
                Double ourFee = (Double) dataSnapshot.child("ourFee").getValue();
                toPay = total - ourFee;

                final Firebase userRef = firebaseref.child("users").child(toRate);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        delivererCustID = (String) dataSnapshot.child("customer_stripe_id").getValue();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //getting the custID of the deliverer

        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRef.child("status").setValue("D");

                new AsyncJob.AsyncJobBuilder<Boolean>()
                        .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                            @Override
                            public Boolean doAsync() {
                                try {
                                    com.stripe.Stripe.apiKey = Constants.STRIPE_API_TEST_SECRET_KEY;

                                    //retrieving customer
                                    Customer deliverer = Customer.retrieve(delivererCustID);
                                    System.out.println(deliverer);
                                    System.out.println(delivererCustID);
                                    System.out.println("NODSFLSKDFJSDF");
                                    String cardToPay = deliverer.getDefaultSource();

                                    Map<String, Object> transferParams = new HashMap<String, Object>();
                                    transferParams.put("amount", ((int) (toPay * 100)));
                                    transferParams.put("source_type", "bank_account");
                                    transferParams.put("bank_account_id", "ba_184Ymh2eZvKYlo2C2elhjcop");
                                    transferParams.put("currency", "usd");
                                    transferParams.put("destination", cardToPay);
                                    transferParams.put("description", "Delivery Fee");

                                    Transfer.create(transferParams);


                                } catch (AuthenticationException e) {
                                    e.printStackTrace();
                                } catch (InvalidRequestException e) {
                                    e.printStackTrace();
                                } catch (APIConnectionException e) {
                                    e.printStackTrace();
                                } catch (CardException e) {
                                    e.printStackTrace();
                                } catch (APIException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                return true;
                            }
                        })
                        .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                            @Override
                            public void onResult(Boolean result) {
                                Intent i = new Intent(foundWaitingActivity.this, ratingUserActivity.class);
                                i.putExtra("UID", toRate);
                                startActivity(i);
                            }
                        }).create().start();
            }
        });


    }

}
