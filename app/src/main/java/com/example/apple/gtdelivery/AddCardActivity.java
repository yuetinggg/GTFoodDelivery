package com.example.apple.gtdelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class AddCardActivity extends Activity {
    public static final String TEST_PUBLISHABLE_KEY = Constants.STRIPE_API_TEST_PUBLISHABLE_KEY;
    public static final String TEST_SECRET_KEY = Constants.STRIPE_API_TEST_SECRET_KEY;

    ArrayList<MenuItem> order;
    private Stripe stripe;
    private EditText cardNumber;
    private EditText expDate;
    private EditText cvc;
    private CircleButton button;
    private Firebase firebaseRef;
    private String customerId;
    private String TAG = "AddCardActivity";
    private RelativeLayout layout;
    private String custEmail;
    private boolean fromConfirmation = false;
    private boolean fromOrderChooser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        layout = (RelativeLayout) findViewById(R.id.add_card_layout);

        //Getting shared preferences to update status
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        custEmail = prefs.getString(Constants.USER_EMAIL, "");
        final SharedPreferences.Editor edit = prefs.edit();

        //If previous screen was confirm order, retrieve the order information
        String activity = getIntent().getStringExtra("activity");
        if (activity != null) {
            if (activity.equals("confirmOrder")) {
                fromConfirmation = true;
                Bundle extras = getIntent().getExtras();
                order = (ArrayList<MenuItem>) extras.getSerializable("ORDER_INFORMATION");
            } else if (activity.equals("orderChooser")){
                fromOrderChooser = true;
            }
        }

        try {
            stripe = new Stripe(TEST_PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });
        cardNumber = (EditText) (findViewById(R.id.card_number));
        expDate = (EditText) (findViewById(R.id.expiration_date));
        cvc = (EditText) (findViewById(R.id.cvc));
        button = (CircleButton) findViewById(R.id.circleButton_addCard);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://gtfood.firebaseio.com/");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Just creating a customerId with a card token attached
                //Creates a token using the card, then "charges" the customer to retrieve the customer id
                // doesn't really charge the customer
                final int expYear = Integer.parseInt(expDate.getText().toString().split("/")[1]);
                final int expMonth = Integer.parseInt(expDate.getText().toString().split("/")[0]);
                Card card = new Card(cardNumber.getText().toString(), expMonth, expYear, cvc.getText().toString());
                Log.d(TAG, "MADE CARD");
                boolean validation = card.validateCard();
                if (validation) {
                  new Stripe().createToken(
                    card, TEST_PUBLISHABLE_KEY, new TokenCallback() {
                      public void onSuccess(final Token token) {
                          try {
                              Log.d(TAG, "TOKEN ID BEFORE: " + token.getId());

                              new AsyncJob.AsyncJobBuilder<Boolean>()
                                      .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                          @Override
                                          public Boolean doAsync() {
                                              try {
                                                  com.stripe.Stripe.apiKey = Constants.STRIPE_API_TEST_SECRET_KEY;

                                                  // Create a Customer
                                                  String tokenId = token.getId();
                                                  Map<String, Object> customerParams = new HashMap<String, Object>();
                                                  customerParams.put("card", tokenId);
                                                  customerParams.put("description", custEmail);

                                                  Customer customer = Customer.create(customerParams);

                                                  //"Charging" the customer to get the card to be saved with the customer
                                                  //Map<String, Object> chargeParams = new HashMap<String, Object>();
                                                  //chargeParams.put("amount", 0); // amount in cents, again
                                                  //chargeParams.put("currency", "usd");
                                                  //chargeParams.put("customer", customer.getId());

                                                  //Charge.create(chargeParams);

                                                  //Saving the customer id
                                                  customerId = customer.getId();

                                                  //Saving the customer id to local storage
                                                  edit.putString("customer_stripe_id", customerId);
                                                  edit.commit();

                                                  //Saving the customer id to Firebase
                                                  firebaseRef.child("users").child(firebaseRef.getAuth().getUid()).child("customer_stripe_id").setValue(customerId);
                                                  //SAVE CUSTOMER ID TO FIREBASE

                                                  // NOW CREATE INTENT
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
                                              Toast.makeText(AddCardActivity.this, "Card Information Saved!", Toast.LENGTH_SHORT).show();

                                              //Sending the user back to the foodChooserActivity
                                              if(fromConfirmation) {
                                                  Intent intent = new Intent(AddCardActivity.this, ConfirmOrder.class);
                                                  intent.putExtra("ORDER_INFORMATION", order);
                                                  startActivity(intent);
                                              } else if (fromOrderChooser) {
                                                  Intent intent = new Intent(AddCardActivity.this, OrderChooserActivity.class);
                                                  startActivity(intent);
                                              } else {
                                                  Intent intent = new Intent(AddCardActivity.this, OrderOrDeliver.class);
                                                  startActivity(intent);
                                              }
                                          }
                                      }).create().start();



                              //Charging client
                              /*Thread t = new Thread() {
                                  public void run() {
                                      chargeClient(token);
                                  }
                              };
                              t.start();*/
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
    private void chargeClient(final Token token) {

        String current_uid = firebaseRef.getAuth().getUid();
        Firebase newRef = new Firebase("https://gtfood.firebaseio.com/status_table/" + current_uid);
        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "TOKEN ID AFTER: " + token.getId());
                final Map<String, Object> chargeParams = new HashMap<String, Object>();
                double total = dataSnapshot.getValue(Order.class).getTotal();
                double ourFee = dataSnapshot.getValue(Order.class).getOurFee();
                double delivererFee = dataSnapshot.getValue(Order.class).getDeliveryFee();
                String email = dataSnapshot.getValue(Order.class).getEmail();

                final Map<String, Object> customerParams = new HashMap<String, Object>();
                customerParams.put("description", email);
                customerParams.put("card", token.getId());

                Log.d(TAG, "EMAIL = " + email);
                chargeParams.put("description", email);
//                chargeParams.put("source", token.getId());
                Log.d(TAG, "AMOUNT AFTER CHANGING = " + total * 100);
                chargeParams.put("amount", (int) (total * 100));
                chargeParams.put("currency", "usd");
                Log.d(TAG, "Charge Params put");
                com.stripe.Stripe.apiKey = Constants.STRIPE_API_TEST_SECRET_KEY;



                new AsyncJob.AsyncJobBuilder<Boolean>()
                        .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                            @Override
                            public Boolean doAsync() {
                                try {
                                    Customer customer = Customer.create(customerParams);
                                    Log.d(TAG, "CUSTOMER ID: " + customer.getId());
                                    chargeParams.put("customer", customer.getId());
                                    Log.d(TAG, "Creating Charge");
                                    Charge.create(chargeParams);
                                    Log.d(TAG, "Charge Created");
                                    //SAVE CUSTOMER ID TO FIREBASE

                                    // NOW CREATE INTENT
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
                                Toast.makeText(AddCardActivity.this, "Charged Card", Toast.LENGTH_SHORT).show();
                            }
                        }).create().start();






            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void handleError(String text) {
        Toast.makeText(AddCardActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    }
