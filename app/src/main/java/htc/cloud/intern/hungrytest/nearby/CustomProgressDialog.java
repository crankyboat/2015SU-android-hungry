package htc.cloud.intern.hungrytest.nearby;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 8/21/15.
 */
public class CustomProgressDialog extends ProgressDialog {

    private Context mContext;

    public CustomProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_map);

        ((ProgressBar)findViewById(R.id.progress_loading))
                .getIndeterminateDrawable().setColorFilter(((Activity)mContext).getResources()
                .getColor(R.color.my_primary), PorterDuff.Mode.MULTIPLY);

        View container = findViewById(R.id.progress_container);
        container.measure(0, 0);
        int widthPadding = container.getPaddingLeft() + container.getPaddingRight();
        int heightPadding = container.getPaddingTop() + container.getPaddingBottom();
        this.getWindow().setLayout(container.getMeasuredWidth()+widthPadding, container.getMeasuredHeight()+heightPadding);

    }

}
