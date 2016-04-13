package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

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

public class ConfirmOrder extends Activity {
    ArrayList<MenuItem> order;
    ListView menu;
    Double total;
    Double deliveryFee = 1.00;
    TextView totalView;
    TextView deliveryFeeView;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        //Get the order from previous activity
        Bundle extras = getIntent().getExtras();
        order = (ArrayList<MenuItem>) extras.getSerializable("ORDER_INFORMATION");

        updateTotal();

        //Setting the numbers, and the decimal format
        df = new DecimalFormat();
        df.setCurrency(Currency.getInstance("USD"));

        totalView = (TextView) findViewById(R.id.Total);
        deliveryFeeView = (TextView) findViewById(R.id.DeliveryFee);

        totalView.setText(df.format(total));
        deliveryFeeView.setText(df.format(deliveryFee));

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

    }

    private void updateTotal() {
        total = 0.0;
        for(MenuItem i : order) {
            total += Double.parseDouble(i.getPrice().substring(1,i.getPrice().length()-1));
        }
        total *= 1.08;
        total += deliveryFee;
    }

}
