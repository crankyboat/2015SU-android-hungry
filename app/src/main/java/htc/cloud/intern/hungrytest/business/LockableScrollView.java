package htc.cloud.intern.hungrytest.business;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 8/11/15.
 */
public class LockableScrollView extends ScrollView {

    private boolean enableLock = true;

    public void setEnableLock(boolean enableLock) {
        this.enableLock = enableLock;
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();
        View transparentView = getRootView().findViewById(R.id.transparent_padding);

        if(enableLock && x > transparentView.getLeft() && x < transparentView.getRight()
                && y > transparentView.getTop() && y < transparentView.getBottom()){
           return false;
        }
        else {
            return super.onInterceptTouchEvent(ev);
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();
        View transparentView = getRootView().findViewById(R.id.transparent_padding);

        if(enableLock && x > transparentView.getLeft() && x < transparentView.getRight()
                && y > transparentView.getTop() && y < transparentView.getBottom()){
            return false;
        }
        else {
            return super.onTouchEvent(ev);
        }

    }

}
