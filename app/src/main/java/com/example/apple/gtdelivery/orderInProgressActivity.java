package com.example.apple.gtdelivery;

import android.os.Bundle;
import android.app.Activity;

public class orderInProgressActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in_progress);

        String message = getIntent().getStringExtra("acceptedOrder");
        

    }

}
