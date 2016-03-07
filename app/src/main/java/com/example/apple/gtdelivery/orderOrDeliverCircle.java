package com.example.apple.gtdelivery;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;

/**
 * Created by yuetinggg on 3/6/16.
 */
public class orderOrDeliverCircle extends View {
    //Keeping tracking of color and the labels of the circle
    private int oColor, dColor, oLabelColor, dLabelColor, lineColor;
    private static String order ="ORDER";
    private static String deliver="DELIVER";
    // Paint objects for when the View is drawn
    private Paint oPaint, dPaint, linePaint;
    private Context thisContext;

    public orderOrDeliverCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        thisContext = context;

        //Create Paint object for OnDraw()
        oPaint = new Paint();
        dPaint = new Paint();
        linePaint = new Paint();

        //Getting the attribute values set in the XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.orderOrDeliverCircle, 0, 0);
        try{
            //getting the colors
            oColor = a.getInteger(R.styleable.orderOrDeliverCircle_oSideColor, 0);
            dColor = a.getInteger(R.styleable.orderOrDeliverCircle_dSideColor, 0);
            oLabelColor = a.getInteger(R.styleable.orderOrDeliverCircle_oSideLabelColor, 0);
            dLabelColor = a.getInteger(R.styleable.orderOrDeliverCircle_dSideLabelColor, 0);
            lineColor = a.getInteger(R.styleable.orderOrDeliverCircle_lineColor, 0);
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
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(5f);

        //Creating the oval for the drawArc to fill
        RectF oval = new RectF(viewWidthHalf - radius, viewHeightHalf - radius, viewWidthHalf + radius, viewHeightHalf + radius);

        //Drawing the circle using the canvas and paint
        canvas.drawArc(oval, -75, 180, true, dPaint);
        canvas.drawArc(oval, 105, 180, true, oPaint);

        //Drawing the line
        canvas.drawLine(viewWidthHalf - (radius / 2), (float) (viewHeightHalf + Math.tan(Math.toRadians(75)) * (radius / 2)), radius / 2 + viewWidthHalf, (float) (viewHeightHalf - Math.tan(Math.toRadians(75)) * (radius / 2)), linePaint);

        //Draw the text
        linePaint.setColor(oLabelColor);
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setTypeface(Typeface.createFromAsset(thisContext.getAssets(), "fonts/YanoneKaffeesatz-Light.ttf"));
        linePaint.setTextSize(100);
        Rect textBounds = new Rect();
        linePaint.getTextBounds("a", 0, 1, textBounds);
        canvas.drawText(order, viewWidthHalf - radius + (radius / 5 * 3), (viewHeightHalf*2 + textBounds.height()) >> 1, linePaint);
        linePaint.setColor(dLabelColor);
        canvas.drawText(deliver, viewWidthHalf + (radius / 5 * 3), (viewHeightHalf*2 + textBounds.height()) >> 1, linePaint);

    }



}
