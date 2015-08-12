package htc.cloud.intern.hungrytest.business;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.R;

public class BusinessActivity extends ActionBarActivity {

    public static final String bName = "BUSINESS-NAME";
    public static final String bAddr = "BUSINESS-ADDR";
    public static final String bCat = "BUSINESS-CAT";
    public static final String bPhone = "BUSINESS-PHONE";
    public static final String bRating = "BUSINESS-RATING";
    public static final String bDist = "BUSINESS-DIST";
    public static final String bSnippet = "BUSINESS-SNIPPET";
    public static final String bImgSrc = "BUSINESS-IMG";
    public static final String bImgList = "BUSINESS-IMGLIST";

    private static final int SWIPE_MIN_DISTANCE = 80;
    private static final int SWIPE_THRESHOLD_VELOCITY = 150;
    private GestureDetector mDetector;
    private Context mContext;
    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.business_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup all views
        Intent mIntent = getIntent();
        ((TextView) findViewById(R.id.business_name)).setText(getIntent().getStringExtra(bName));
        ((TextView) findViewById(R.id.business_address)).setText(getIntent().getStringExtra(bAddr));
        ((TextView) findViewById(R.id.business_category_text)).setText(getIntent().getStringExtra(bCat));
        ((TextView) findViewById(R.id.business_phone)).setText(getIntent().getStringExtra(bPhone));
        ((TextView) findViewById(R.id.business_dist)).setText(Math.round(getIntent().getDoubleExtra(bDist, 0))+" km");
        ((RatingBar) findViewById(R.id.business_rating)).setRating(getIntent().getFloatExtra(bRating, 0));
//        ((TextView) findViewById(R.id.business_snippet)).setText(getIntent().getStringExtra(bSnippet));

//        Ion.with((ImageView)findViewById(R.id.business_image))
//                .placeholder(R.drawable.ic_stars_black_24dp)
//                .load(getIntent().getStringExtra(bImgSrc).replace("ls.jpg", "o.jpg"));

//        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra(bPhone)));
//                startActivity(callIntent);
//            }
//        });

        // Setup ViewFlipper
        mContext = this;
        mDetector = new GestureDetector(new SwipeGestureDetector());
        mViewFlipper = (ViewFlipper) findViewById(R.id.business_viewflipper);

        // Add all ImageViews to ViewFlipper
        final ArrayList<String> imgList = getIntent().getStringArrayListExtra(BusinessActivity.bImgList);
        if ( imgList.size() > 0 ) {
            for  (int i = 0; i < imgList.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Ion.with(imageView)
                        .placeholder(R.drawable.ic_stars_black_24dp)
                        .load(imgList.get(i).replace("ls.jpg", "o.jpg"));
                mViewFlipper.addView(imageView);
            }
        }
        else {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.business_placeholder);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mViewFlipper.addView(imageView);
        }

        // Setup dynamic ScrollView and top-level touch event on TransparentView
        final View scrollView = (View) findViewById(R.id.transparent_padding).getParent().getParent();
        final View transparentView = (View) findViewById(R.id.transparent_padding);
        final View contentView = (View) findViewById(R.id.business_content_bg);
        final double scrollMax = 600;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = scrollView.getScrollY();
                        if (scrollY <= scrollMax) {
                            contentView.setAlpha((float)0.5+(float)(0.5*scrollY/scrollMax));
                            transparentView.setAlpha(Math.min((float) (scrollY / (scrollMax - 10)), (float) 1.0));
                        }

                        if (scrollY <= 0 && imgList.size() > 1) {
                            transparentView.setEnabled(true);
                            ((LockableScrollView)scrollView).setEnableLock(true);
                        }
                        else {
                            transparentView.setEnabled(false);
                            ((LockableScrollView)scrollView).setEnableLock(false);
                        }
                    }
                });

        transparentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (mViewFlipper.getChildCount() > 1) {
                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                        mViewFlipper.showNext();
                        return true;
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));
                        mViewFlipper.showPrevious();
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
