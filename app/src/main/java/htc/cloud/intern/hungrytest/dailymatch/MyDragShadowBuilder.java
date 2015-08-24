package htc.cloud.intern.hungrytest.dailymatch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by cwhuang on 2015/8/24.
 */
public class MyDragShadowBuilder extends View.DragShadowBuilder {

    /*View v;
    public MyDragShadowBuilder(View v) {
        super(v);
        this.v=v;
    }
    @Override
    public void onDrawShadow(Canvas canvas) {
        super.onDrawShadow(canvas);



        canvas.drawBitmap(getBitmapFromView(v), 0, 0, null);
    }
    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point touchPoint) {
        int width,height;
        width = (int)getView().getX()/2;
        height =  (int)getView().getY()/2;
        shadowSize.set(getView().getWidth(),getView().getHeight());
        //shadowSize.set((int)v.getX(),(int)v.getY());
        //touchPoint.set(v.getWidth(), v.getHeight());
        touchPoint.set(width,height);

    }*/
    /*
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }*/

    private float x, y;

    public MyDragShadowBuilder(View view, float x, float y) {
        super(view);
        this.x = x;
        this.y = y;
    }

    @Override
    public void onProvideShadowMetrics(Point size, Point touchPoint) {
        super.onProvideShadowMetrics(size, touchPoint);
        touchPoint.set((int) x, (int) y);
    }


}
