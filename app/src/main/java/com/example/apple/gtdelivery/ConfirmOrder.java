package com.example.apple.gtdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import com.arasthel.asyncjob.AsyncJob;
import com.firebase.client.Firebase;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import at.markushi.ui.CircleButton;

public class ConfirmOrder extends Activity {
    ArrayList<MenuItem> order;
    ListView menu;
    Double total;
    Double deliveryFee = 2.00;
    TextView totalView;
    TextView deliveryFeeView;
    DecimalFormat df;
    AutoCompleteTextView location;
    CircleButton done;
    Context current;
    localUser user;
    Double ourFee = 0.0;
    String customer_stripe_id;
    private String TAG = "ConfirmOrderActivity";
    SharedPreferences prefs;
    String chargeID;

    Firebase firebaseRef = new Firebase("https://gtfood.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Firebase.setAndroidContext(this);
        user = new localUser(this);

        //Getting shared preferences to update status
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        System.out.println(prefs.getString("Email", "") + "SHIT");
        customer_stripe_id = prefs.getString("customer_stripe_id", "");
        System.out.println(customer_stripe_id + "20");
        final SharedPreferences.Editor edit = prefs.edit();

        //Get the order from previous activity
        Bundle extras = getIntent().getExtras();
        order = (ArrayList<MenuItem>) extras.getSerializable("ORDER_INFORMATION");
        current = this;

        updateTotal();

        //Setting the numbers, and the decimal format
        df = new DecimalFormat("$#.00");

        totalView = (TextView) findViewById(R.id.Total);
        deliveryFeeView = (TextView) findViewById(R.id.DeliveryFee);

        totalView.setText(df.format(total));
        deliveryFeeView.setText(df.format(deliveryFee));

        //Setting up the autocomplete text view for the location
        location = (AutoCompleteTextView) findViewById(R.id.locationList);
        String s = "430 10th Street NW Building,890 Curran St NW,A French Building,Academy of Medicine,Advanced Wood Products,Aerospace Engineering Combustion Lab,Ajax Building,Alexander Memorial Coliseum,Alpha Chi Omega,Alpha Delta Chi,Alpha Delta Pi,Alpha Epsilon Pi,Alpha Gamma Delta,Alpha Tau Omega,Alpha Xi Delta,Alumni House,Alumni Park,Aquatic Center,Architecture Building East,Architecture Building West,Armstrong Residence,Army Armory,Athletic Association Conference Room,Baker Building,Baptist Student Union,Beringause Building,Beta Theta Pi,Bill Moore Student Success Center,Bill Moore Tennis Center,Bobby Dodd Stadium,Boggs Building,Brittain Dining Hall,Broadband Institute Residential Laboratory,Brown Residence,Building Construction and Center for GIS,Bunger Henry Building,Burger Bowl Field,Business Services Building,CEISMC,Caldwell Residence,Callaway Building,Campus Recreation Center,Carbon Neutral Energy Solutions Laboratory,Carnegie Building,Catholic Center,Centennial Research Building,Center Street Apartments,Center Street North,Center Street South,Center for Assistive Technology and Environmental Access,Centergy One,Central Receiving Property Control,Chandler Stadium,Chapin Building,Cherry Emerson,Cherry Street Library,Chi Phi,Chi Psi,Christian Campus Fellowship,Cloudman Residence,Clough Undergraduate Learning Commons,College of Architecture,College of Computing,College of Management,Commander Building,Coon Building,Couch Building,Crecine Apartments,Crosland Tower Northwest Library,Custodial Services Building,DM Smith Building,Daniel Laboratory,Delta Chi,Delta Sigma Phi,Delta Tau Delta,Delta Upsilon,Digital Fabrication Lab,Economic Development Building,Edge Athletic Center,Eighth Street Apartments,Engineered Biosystems Building,Engineering Center,Engineering Science and Mechanics Building,Engineers Bookstore,Facilities Building,Family Housing,Ferst Center for the Arts,Fiber Optic Network Building,Field Residence Hall,Fitten Residence Hall,Folk Residence Hall,Food Processing Technology Building,Ford Environmental Science and Technology Building,Fourth Street Apartments,Freeman Residence Hall,Fulmer Residence Hall,GTRI Conference Center,Georgia Tech Competition Center,Georgia Tech Global Learning Center,Georgia Tech Hotel and Conference Center,Georgia Tech Water Sports,Georgia Tech Yellow Jacket Ticketing Office,Glenn Residence Hall,Graduate Living Center,Griffin Track,Grinnell Building,Groseclose Building and ISyE Annex,Guggenheim Building,Habersham Building,Hall Building,Hanson Residence Hall,Harris Residence Hall,Harrison Residence Hall,Health Systems Institute,Hefner Residence Hall,Hemphill Avenue Apartments,Hinman Research Building,Holland Building,Hopkins Residence Hall,Howell Residence Hall,Howey Physics Building And Observatory,Institute of Paper Science and Technology,Instructional Center,Ivan Allen College,JC Shaw Sports Complex Athletic Association,Juniors Grill,Kappa Alpha,Kappa Sigma,Ken Byers Tennis Complex,Kessler Campanile,King Facilities Building,Klaus Advanced Computing Bldg,Klaus Advanced Computing Building,Lamar Allen Sustainable Education Building,Lambda Chi Alpha,Landscape Services,Love Building,Luck Building,Lutheran Center,Lyman Hall,Manufacturing Related Disiplines Complex,Manufacturing Research Center,Marcus Nanotechnology Building,Mason Civil Engineering Building,Matheson Residence Hall,Maulding Residence Hall,McCamish Pavilion,Mechanical Engineering Research Building,Mewborn Field,Molecular Science And Engineering Building,Montag Residence Hall,Montgomery Knight Building,Neely Nuclear Research Center,North Avenue Apartments,North Avenue Dining Hall,North Avenue East,North Avenue North,North Avenue South,North Avenue South Apartments,North Avenue West,North View Apartments,OIT Engineering,OKeefe Building,OKeefe Gym,Office of Human Resources,Office of Information Technology,Old CE Building,Parker H Petit Biotechnology Building,Paul Hefferna House,Perry Residence Hall,Pettit Microelectronics Research Center,Phi Delta Theta,Phi Gamma Delta,Phi Kappa Sigma,Phi Kappa Tau,Phi Kappa Theta,Phi Mu,Phi Sigma Kappa,Pi Kappa Alpha,Pi Kappa Alpha House,Pi Kappa Phi,Prince Gilbert Northwest Library,Psi Upsilon,R Kirk Landon Learning Center,Research Administration,Rice Center for Sports Performance,Rich Computer Center,Savant Building,Scheller College of Business,School of Applied Physiology,School of Physics,Sigma Alpha Epsilon,Sigma Chi,Sigma Nu,Sigma Phi Epsilon,Sixth Street Apartments,Skiles Classroom Building,Smith Residence Hall,Smithgall Student Services Building,Stamps Student Center Commons,Stein Hayes Goldin And Fourth Street E Houses,Structural Engineering and Materials Research Lab,Student Health Center,Student Health Services,Swann Building,Tau Kappa Epsilon,Tech Tower,Technology Square Research Building,Tenth and Home,Theta Chi,Theta Xi,Towers Residence Hall,UA Whitaker Building,Undergraduate Living Center,Van Leer School Of Electrical And Computer Engineering,Veron D and Helen D Crawford Pool,WH Emerson Building,Wardlaw Center,Weber Space Science And Technology Building Ii,Wenn Student Center,Wesley Foundation/Methodist Center,Westminster Christian Fellowship,Whitehead Building Health Center,Woodruff Residence Hall,Zelnak Basketball Practice Facility,Zeta Beta Tau,Zeta Tau Alpha";
        String[] LOCATIONS = s.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LOCATIONS);
        location.setAdapter(adapter);

        //Creating the adapter for the order and finding views
        menu = (ListView) findViewById(R.id.orderList);


        BindDictionary<MenuItem> dictionary = new BindDictionary<>();
        dictionary.addStringField(R.id.foodName, new StringExtractor<MenuItem>() {
            @Override
            public String getStringValue(MenuItem menuItem, int i) {
                return menuItem.getName();
            }
        });

        dictionary.addStringField(R.id.foodPrice, new StringExtractor<MenuItem>() {
            @Override
            public String getStringValue(MenuItem menuItem, int i) {
                return menuItem.getPrice();
            }
        });

        FunDapter<MenuItem> adapt = new FunDapter<>(this, order, R.layout.order_confirm_item, dictionary);

        menu.setAdapter(adapt);

        System.out.println(customer_stripe_id);
        System.out.println("HELLOOOOOOOOOOOO");
        //Setting on click listener for done button
        done = (CircleButton) findViewById(R.id.circleButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.getText() == null || location.getText().toString() == "") {
                    Toast.makeText(current, "Please select delivery location.", Toast.LENGTH_LONG);
                } else if (customer_stripe_id.equals("notSet")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConfirmOrder.this);
                    alert.setTitle("No Payment Info Added");
                    final String message = "Please add your card info to proceed with the order. Add card now?";
                    alert.setMessage(message).setCancelable(true).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(ConfirmOrder.this, AddCardActivity.class);
                            i.putExtra("activity", "confirmOrder");
                            i.putExtra("ORDER_INFORMATION", order);
                            startActivity(i);
                        }
                    }).show();
                } else {
                    //charging the card
                    new AsyncJob.AsyncJobBuilder<Boolean>()
                            .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                @Override
                                public Boolean doAsync() {
                                    try {
                                        com.stripe.Stripe.apiKey = Constants.STRIPE_API_TEST_SECRET_KEY;

                                        //"Charging" the customer to get the card to be saved with the customer
                                        Map<String, Object> chargeParams = new HashMap<String, Object>();
                                        chargeParams.put("amount", ((int) (total * 100))); // amount in cents, again
                                        chargeParams.put("currency", "usd");
                                        chargeParams.put("customer", prefs.getString("customer_stripe_id", ""));

                                        Charge object = Charge.create(chargeParams);
                                        chargeID = object.getId();
                                        System.out.println(chargeID);


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
                                    Toast.makeText(ConfirmOrder.this, "Charged Card", Toast.LENGTH_SHORT).show();

                                    edit.putString("Status", "O");
                                    edit.commit();

                                    Firebase userRef = firebaseRef.child("status_table").child(firebaseRef.getAuth().getUid());
                                    Firebase thisUser = firebaseRef.child("users").child(firebaseRef.getAuth().getUid()).child("status");
                                    thisUser.setValue("O");

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    ArrayList foodItems = new ArrayList<>();
                                    for (MenuItem m : order) {
                                        foodItems.add(m.getName());
                                    }
                                    //Assumes that the restaurant is the same restaurant
                                    String restaurant = order.get(0).getrName();
                                    map.put("email", user.getEmail());
                                    map.put("ordererName", user.getName());
                                    map.put("ordererRating", user.getRating());
                                    map.put("foodItems", foodItems);
                                    map.put("restaurant", restaurant);
                                    map.put("deliveryFee", deliveryFee);
                                    map.put("ourFee", ourFee);
                                    map.put("total", total);
                                    map.put("deliveryLocation", location.getText().toString());
                                    map.put("status", Constants.ORDER_REQUESTED);
                                    map.put("delivererUID", "notSet");
                                    userRef.updateChildren(map);
                                    Intent intent = new Intent(ConfirmOrder.this, OrderSearchActivity.class);
                                    intent.putExtra("chargeID", chargeID);
                                    startActivity(intent);
                                    
                                }
                            }).create().start();

                }
            }
        });

    }

    private void updateTotal() {
        total = 0.0;
        for(MenuItem i : order) {
            total += Double.parseDouble(i.getPrice().substring(1,i.getPrice().length()-1));
        }
        ourFee = 0.1*total;
        total += ourFee;
        total += deliveryFee;
    }

}
