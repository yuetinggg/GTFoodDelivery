package com.example.apple.gtdelivery;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class OrderSearchActivity extends Activity {
    ImageView redCircleImage;
    ImageButton blueButton;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);
        redCircleImage = (ImageView) findViewById(R.id.imageView);
        blueButton = (ImageButton) findViewById(R.id.imageButton);
        context = this;
        Picasso.with(context).load(R.drawable.red_circle).into(redCircleImage);
        Picasso.with(context).load(R.drawable.blue_button).into(blueButton);
    }

}
