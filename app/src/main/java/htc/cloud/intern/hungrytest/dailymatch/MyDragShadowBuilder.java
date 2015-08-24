package htc.cloud.intern.hungrytest.dailymatch;

import android.graphics.Point;
import android.view.View;

/**
 * Created by cwhuang on 2015/8/24.
 */
public class MyDragShadowBuilder extends View.DragShadowBuilder {

    private float x, y;
    private View v;

    public MyDragShadowBuilder(View view, float x, float y) {
        super(view);
        v = view;
        this.x = x;
        this.y = y;
    }

    @Override
    public void onProvideShadowMetrics(Point size, Point touchPoint) {
        super.onProvideShadowMetrics(size, touchPoint);
        touchPoint.set((int) x, (int) y);
    }


}
