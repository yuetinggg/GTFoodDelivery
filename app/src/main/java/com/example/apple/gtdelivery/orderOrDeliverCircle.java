package com.example.apple.gtdelivery;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;

/**
 * Created by yuetinggg on 3/6/16.
 */
public class orderOrDeliverCircle extends View {
    //Keeping tracking of color and the labels of the circle
    private int oColor, dColor, oLabelColor, dLabelColor;
    private static String order ="ORDER";
    private static String deliver="DELIVER";
    // Paint objects for when the View is drawn
    private Paint oPaint, dPaint;

    public orderOrDeliverCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Create Paint object for OnDraw()
        oPaint = new Paint();
        dPaint = new Paint();

        //Getting the attribute values set in the XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.orderOrDeliverCircle, 0, 0);
        try{
            //getting the colors
            oColor = a.getInteger(R.styleable.orderOrDeliverCircle_oSideColor, 0);
            dColor = a.getInteger(R.styleable.orderOrDeliverCircle_dSideColor, 0);
            oLabelColor = a.getInteger(R.styleable.orderOrDeliverCircle_oSideLabelColor, 0);
            dLabelColor = a.getInteger(R.styleable.orderOrDeliverCircle_dSideLabelColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Creating the Circle
        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;
        int radius = (int) (viewWidthHalf * 0.75);

        //Setting the paint properties
        oPaint.setStyle(Paint.Style.FILL);
        oPaint.setColor(oColor);
        dPaint.setStyle(Paint.Style.FILL);
        dPaint.setColor(dColor);

        //Creating the oval for the drawArc to fill
        RectF oval = new RectF(viewWidthHalf - radius, viewHeightHalf - radius, viewWidthHalf + radius, viewHeightHalf + radius);

        //Drawing the circle using the canvas and paint
        canvas.drawArc(oval, -75, 180, true, dPaint);
        canvas.drawArc(oval, 106, 180, true, oPaint);

    }



}
