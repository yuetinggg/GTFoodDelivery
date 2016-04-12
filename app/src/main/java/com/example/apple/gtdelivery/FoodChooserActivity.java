package com.example.apple.gtdelivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class FoodChooserActivity extends Activity implements View.OnClickListener {
    Context current;
    String[] iconString = {"chick.png", "tacoBell.png", "pandaExpress.png"};
    LinearLayout iconScroll;
    ArrayList<RoundedImageView> iconViews;
    ListView menu;
    List<List<MenuItem>> allMenus;
    Firebase firebaseRef = new Firebase("https://gtfood.firebaseio.com/");
    CircleButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_chooser);
        current = this;
        Firebase.setAndroidContext(this);

        //Hardcoding menus for now
        //Chick-fil-a menu
        String c = "Chick-Fil-A";
        List<MenuItem> chick = new ArrayList<MenuItem>();
        chick.add(new MenuItem(c, "Milkshake", "$3.99"));
        chick.add(new MenuItem(c, "Burger", "$5.99"));
        chick.add(new MenuItem(c, "Fries", "$4.99"));

        //tacoBell menu
        String t = "Taco Bell";
        List<MenuItem> taco = new ArrayList<MenuItem>();
        taco.add(new MenuItem(t, "Nachos", "$3.99"));
        taco.add(new MenuItem(t, "Salad", "$5.99"));
        taco.add(new MenuItem(t, "Burrito", "$4.99"));

        //pandaExpress menu
        String p = "Panda Express";
        List<MenuItem> panda = new ArrayList<MenuItem>();
        panda.add(new MenuItem(p, "Beijing Beef", "$3.99"));
        panda.add(new MenuItem(p, "Chicken Terriyaki", "$5.99"));
        panda.add(new MenuItem(p, "Broccoli", "$4.99"));

        allMenus = new ArrayList<List<MenuItem>>();
        allMenus.add(chick);
        allMenus.add(taco);
        allMenus.add(panda);


        // Add restaurant icons to the horizontal view
        iconScroll = (LinearLayout) findViewById(R.id.iconScroll);

        //Retrieving the listview for menu
        menu = (ListView) findViewById(R.id.menuList);
        MenuItemAdapter adapter = new MenuItemAdapter(allMenus.get(allMenus.size()/2), current);
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new MenuOnItemClickListener(allMenus.get(allMenus.size()/2)));

        //Dealing with the onclick for each icon
        iconViews = new ArrayList<RoundedImageView>();

        //Getting bitmaps for the icons and adding to linearLayout inside HorizontalScrollView
        for (int i = 0; i < iconString.length; i++) {
            Bitmap currentIcon = getBitmapFromAsset(this, "bitmap/" + iconString[i]);
            currentIcon = Bitmap.createScaledBitmap(currentIcon, 200, 200, false);

            //Create roundedImageView for each icon
            RoundedImageView r = new RoundedImageView(this);
            iconViews.add(r);
            //Log.d("MyApp", "RID: " + r.getId());
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
        
        button = (CircleButton) findViewById(R.id.circleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(FoodChooserActivity.this, OrderSearchActivity.class);
                Intent intent = new Intent(FoodChooserActivity.this, LocationSelector.class);
                startActivity(intent);
            }
        });
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
        //Log.d("MyApp", "Click:" + i);
        while (i < iconViews.size() && iconViews.get(i) != v) {
            i++;
        }
        MenuItemAdapter adapter = new MenuItemAdapter(allMenus.get(i), current);
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new MenuOnItemClickListener(allMenus.get(i)));
    }
    private class MenuOnItemClickListener implements AdapterView.OnItemClickListener {
        private List<MenuItem> items;
        public MenuOnItemClickListener(List<MenuItem> items) {
            this.items = items;
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String message = "Restaurant Name: " + items.get(position).getrName() + "\n" +
                    "Food Item: " + items.get(position).getName() + "\n" +
                    "Price: " + items.get(position).getPrice();
            AlertDialog.Builder alert = new AlertDialog.Builder(FoodChooserActivity.this);
            alert.setTitle("Confirm Order");
            final int pos = position;
            alert.setMessage(message).setCancelable(false).
                    setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).
                    setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            System.out.println("UID: " + firebaseRef.getAuth().getUid());
                            Firebase userRef = firebaseRef.child("status_table").child(firebaseRef.getAuth().getUid());
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Food Item", items.get(pos).getName());
                            map.put("Restaurant", items.get(pos).getrName());
                            map.put("Price", items.get(pos).getPrice());
                            userRef.updateChildren(map);
                        }
                    }).show();
        }
    }
}
