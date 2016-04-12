package com.example.apple.gtdelivery;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yuetinggg on 3/8/16.
 */
public class MenuItemAdapter extends BaseAdapter {
    Context context;
    protected List<MenuItem> items;
    LayoutInflater inflater;

    public MenuItemAdapter(List<MenuItem> items, Context context) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the menu item into the temporary view holder
        ViewHolder holder = new ViewHolder();
        convertView = this.inflater.inflate(R.layout.menu_item, parent, false);
        holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
        holder.itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
        convertView.setTag(holder);

        //Setting the name and price
        MenuItem item = items.get(position);
        holder.itemName.setText(item.getName().toUpperCase());
        holder.itemPrice.setText(item.getPrice());

        //Making the format of the list
        Context c = convertView.getContext();
        holder.itemName.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/YanoneKaffeesatz-Light.ttf"));
        holder.itemPrice.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/BADABB.ttf"));
        holder.itemName.setTextSize(20);
        holder.itemPrice.setTextSize(20);

        return convertView;
    }

    private class ViewHolder{
        TextView itemName;
        TextView itemPrice;
    }
}
