package com.example.apple.gtdelivery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yuetinggg on 3/6/16.
 */
public class restaurantIcon extends View {
    private int borderColor;
    private Bitmap icon;

    public restaurantIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.restaurantIcon, 0, 0);
        borderColor = a.getInteger(R.styleable.restaurantIcon_borderColor, 0);
    }

    public void setBorderColor(int color) {
        borderColor = color;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
