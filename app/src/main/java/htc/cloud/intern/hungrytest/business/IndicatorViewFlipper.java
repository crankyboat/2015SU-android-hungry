package htc.cloud.intern.hungrytest.business;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * Created by intern on 8/11/15.
 */

public class IndicatorViewFlipper extends ViewFlipper
{

    Paint mPaint = new Paint();

    public IndicatorViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getWidth();

        float margin = 8;
        float radius = 8;
        float cx = width / 2 - ((radius + margin) * 2 * getChildCount() / 2);
        float cy = getHeight() - 40;

        canvas.save();

        for (int i = 0; i < getChildCount(); i++)
        {
            if (i == getDisplayedChild())
            {
                mPaint.setColor(Color.WHITE);
                mPaint.setAlpha((int)Math.round(255*0.8));
                canvas.drawCircle(cx, cy, radius, mPaint);

            } else
            {
                mPaint.setColor(Color.LTGRAY);
                mPaint.setAlpha((int) Math.round(255 * 0.6));
                canvas.drawCircle(cx, cy, radius, mPaint);
            }
            cx += 2 * (radius + margin);
        }
        canvas.restore();
    }

}



