package com.example.apple.gtdelivery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderChooserActivity extends Activity {
    Firebase firebaseref;
    ArrayList<Order> availableOrders;
    ListView orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_chooser);

        //Setting up the listview
        orders = (ListView) findViewById(R.id.orderList);

        //Setting up Firebase context
        Firebase.setAndroidContext(this);
        firebaseref = new Firebase(Constants.BASE_URL);
        Firebase statusTableRef = firebaseref.child("status_table");

        Query orderQuery = statusTableRef.orderByChild("status").equalTo(Constants.ORDER_REQUESTED);

        //Setting up current available orders via querying Firebase
        //Current available orders are not sorted at all, ie. the deliverer has to go
        //through every order to find one that is at the same location as them
        //TODO: Implement sorting logic
        availableOrders = new ArrayList<>();
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println(child);
                    Order order = child.getValue(Order.class);
                    order.setUid(child.getKey());
                    availableOrders.add(order);
                }
                setUpList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void setUpList() {
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

        dictionary.addStringField(R.id.location, new StringExtractor<Order>() {
            @Override
            public String getStringValue(Order order, int i) {
                return order.getDeliveryLocation();
            }
        });

        dictionary.addStringField(R.id.restaurant, new StringExtractor<Order>() {
            @Override
            public String getStringValue(Order order, int i) {
                return order.getRestaurant();
            }
        });

        FunDapter<Order> adapter = new FunDapter<>(this, availableOrders, R.layout.available_orders_list_item, dictionary);

        //Setting the listview adapter
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(new OrderOnItemClickListener(availableOrders));
    }

    private class OrderOnItemClickListener implements AdapterView.OnItemClickListener {
        private List<Order> orders;
        public OrderOnItemClickListener(List<Order> orders) {
            this.orders = orders;
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;
            AlertDialog.Builder alert = new AlertDialog.Builder(OrderChooserActivity.this);
            alert.setTitle("Accept Order");
            final Order order = orders.get(pos);
            String order_items = "";
            for (int i = 0; i < order.getFoodItems().size(); i++) {
                if (i < order.getFoodItems().size() - 1) {
                    order_items += (order.getFoodItems().get(i) + ", ");
                } else {
                    order_items += (order.getFoodItems().get(i));
                }
            }

            DecimalFormat df = new DecimalFormat("#.00");
            final String message = "Name: " + order.getOrdererName() + "\n" +
                             "Order Items: " + order_items + "\n" +
                             "Restaurant: " + order.getRestaurant() + "\n" +
                             "Deliver to: " + order.getDeliveryLocation() + "\n" +
                             "Your fee: $" + df.format(order.getDeliveryFee()) + "\n" +
                             "Orderer's Total: $" + df.format(order.getTotal() - order.getOurFee());
            alert.setMessage(message).setCancelable(true).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Firebase o = firebaseref.child("status_table").child(orders.get(pos).getUid());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("status", Constants.ORDER_ACCEPTED);
                    o.updateChildren(map);
                    Intent i = new Intent(OrderChooserActivity.this, orderInProgressActivity.class);

                    i.putExtra("acceptedOrder", message);
                    i.putExtra("ordererUID", order.getUid());
                    startActivity(i);
                }
            }).show();
        }
    }

}
