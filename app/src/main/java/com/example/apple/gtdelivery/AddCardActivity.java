package com.example.apple.gtdelivery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class AddCardActivity extends Activity {
    public static final String TEST_PUBLISHABLE_KEY = "pk_test_0KPlH4Hw2FUzR7bakDWXZqr1";
    private Stripe stripe;
    private EditText cardNumber;
    private EditText expDate;
    private EditText cvc;
    private CircleButton button;
    private Firebase firebaseRef;
    private String email;
    private String customerId;
    private String TAG = "AddCardActivity";
    private double ourFee;
    private double total;
    private double delivererFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        try {
            stripe = new Stripe(TEST_PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        cardNumber = (EditText) (findViewById(R.id.card_number));
        expDate = (EditText) (findViewById(R.id.expiration_date));
        cvc = (EditText) (findViewById(R.id.cvc));
        button = (CircleButton) findViewById(R.id.circleButton_addCard);
        firebaseRef = new Firebase("https://gtfood.firebaseio.com/");

        final int expYear = Integer.parseInt(expDate.toString().split("/")[1]);
        final int expMonth = Integer.parseInt(expDate.toString().split("/")[0]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = new Card(cardNumber.toString(), expMonth, expYear, cvc.toString());
                boolean validation = card.validateCard();
                if (validation) {
                    new Stripe().createToken(
                            card,
                            TEST_PUBLISHABLE_KEY,
                            new TokenCallback() {
                                public void onSuccess(final Token token) {
                                    try{
//                                        com.stripe.Stripe.apiKey = TEST_PUBLISHABLE_KEY;
//                                        Map<String, Object> customerParams = new HashMap<String, Object>();
//                                        customerParams.put("description", getEmail());
//                                        customerParams.put("card", token.getId());
                                        getDetails();
                                        Thread t = new Thread() {
                                          public void run() {
                                              chargeClient(token);
                                          }
                                        };
                                        t.start();
                                    } catch (Exception e) {
                                        handleError(e.getMessage());
                                    }
                                }
                                public void onError(Exception error) {
                                    handleError(error.getLocalizedMessage());
                                }
                            });
                } else if (!card.validateNumber()) {
                    handleError("The card number that you entered is invalid");
                } else if (!card.validateExpiryDate()) {
                    handleError("The expiration date that you entered is invalid");
                } else if (!card.validateCVC()) {
                    handleError("The CVC code that you entered is invalid");
                } else {
                    handleError("The card details that you entered are invalid");
                }
            }
            }
        );

    }
    private void chargeClient(Token token) {
        com.stripe.Stripe.apiKey = TEST_PUBLISHABLE_KEY;
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("description", getEmail());
        chargeParams.put("card", token.getId());
        chargeParams.put("amount", (int) (total * 100));
        chargeParams.put("currency", "USD");
        Log.d(TAG, "Charge Params put");
        try {
            Charge.create(chargeParams);
            Log.d(TAG, "Charge Created");
            // NOW CREATE INTENT
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
    private String getEmail() {
        String current_uid = firebaseRef.getAuth().getUid();
        Firebase newRef = new Firebase("https://gtfood.firebaseio.com/users/" + current_uid);
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue(localUser.class).getEmail();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return email;
    }
    private void getDetails() {
        String current_uid = firebaseRef.getAuth().getUid();
        Firebase newRef = new Firebase("https://gtfood.firebaseio.com/status_table/" + current_uid);
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = dataSnapshot.getValue(Order.class).getTotal();
                ourFee = dataSnapshot.getValue(Order.class).getOurFee();
                delivererFee = dataSnapshot.getValue(Order.class).getDeliveryFee();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void handleError(String text) {
        Toast.makeText(AddCardActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
