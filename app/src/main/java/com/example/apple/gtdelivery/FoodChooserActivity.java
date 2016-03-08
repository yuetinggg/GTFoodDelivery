package com.example.apple.gtdelivery;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FoodChooserActivity extends Activity implements View.OnClickListener {
    Context current;
    String[] iconString = {"chick.png", "tacoBell.png", "pandaExpress.png"};
    LinearLayout iconScroll;
    ArrayList<RoundedImageView> iconViews;
    ListView menu;
    List<List<menuItem>> allMenus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_chooser);
        current = this;

        //Hardcoding menus for now
        //Chick-fil-a menu
        String c = "Chick-Fil-A";
        List<menuItem> chick = new ArrayList<menuItem>();
        chick.add(new menuItem(c, "Milkshake", "$3.99"));
        chick.add(new menuItem(c, "Burger", "$5.99"));
        chick.add(new menuItem(c, "Fries", "$4.99"));

        //tacoBell menu
        String t = "Taco Bell";
        List<menuItem> taco = new ArrayList<menuItem>();
        taco.add(new menuItem(t, "Nachos", "$3.99"));
        taco.add(new menuItem(t, "Salad", "$5.99"));
        taco.add(new menuItem(t, "Burrito", "$4.99"));

        //pandaExpress menu
        String p = "Panda Express";
        List<menuItem> panda = new ArrayList<menuItem>();
        panda.add(new menuItem(p, "Beijing Beef", "$3.99"));
        panda.add(new menuItem(p, "Chicken Terriyaki", "$5.99"));
        panda.add(new menuItem(p, "Broccoli", "$4.99"));

        allMenus = new ArrayList<List<menuItem>>();
        allMenus.add(chick);
        allMenus.add(taco);
        allMenus.add(panda);


        // Add restaurant icons to the horizontal view
        iconScroll = (LinearLayout) findViewById(R.id.iconScroll);

        //Retrieving the listview for menu
        menu = (ListView) findViewById(R.id.menuList);
        menuItemAdapter adapter = new menuItemAdapter(allMenus.get(allMenus.size()/2), current);
        menu.setAdapter(adapter);

        //Dealing with the onclick for each icon
        iconViews = new ArrayList<RoundedImageView>();

        //Getting bitmaps for the icons and adding to linearLayout inside HorizontalScrollView
        for (int i = 0; i < iconString.length; i++) {
            Bitmap currentIcon = getBitmapFromAsset(this, "bitmap/" + iconString[i]);
            currentIcon = Bitmap.createScaledBitmap(currentIcon, 200, 200, false);

            //Create roundedImageView for each icon
            RoundedImageView r = new RoundedImageView(this);
            iconViews.add(r);
            Log.d("MyApp", "RID: " + r.getId());
            r.setPadding(20, 20, 20, 20);
            r.setScaleType(ImageView.ScaleType.CENTER_CROP);
            r.setCornerRadius((float) 30);
            r.setBorderWidth((float) 20);
            r.setBorderColor(getResources().getColor(R.color.primaryPink));
            r.setImageBitmap(currentIcon);
            r.setOval(true);

            //Setting the onClickListener to change the menu below
            r.setOnClickListener(this);

            //Add to the scroll
            iconScroll.addView(r);
        }
    }

    private static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            Log.e("MYAPP", "exception", e);
            return null;
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        int i = 0;
        Log.d("MyApp", "Click:" + i);
        while (i < iconViews.size() && iconViews.get(i) != v) {
            i++;
        }
        menuItemAdapter adapter = new menuItemAdapter(allMenus.get(i), current);
        menu.setAdapter(adapter);
    }
}
