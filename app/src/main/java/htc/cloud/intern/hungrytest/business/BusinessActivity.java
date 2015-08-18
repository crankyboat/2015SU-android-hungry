package htc.cloud.intern.hungrytest.business;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.hungryapi.AsyncResponse;
import htc.cloud.intern.hungrytest.hungryapi.ReviewAsyncTask;
import htc.cloud.intern.hungrytest.nearbyapi.ListBaseAdapter;

public class BusinessActivity extends ActionBarActivity
    implements AsyncResponse {

    public static final String bId = "BUSINESS-ID";
    public static final String bName = "BUSINESS-NAME";
    public static final String bLatLng = "BUSINESS-LATLNG";
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
    private ListView mListView;
    private ArrayList<ReviewItem> mReviewList = new ArrayList<ReviewItem>();
    private ArrayList<ReviewItem> mShortReviewList = new ArrayList<ReviewItem>();
    private ReviewListBaseAdapter mReviewListAdapter;
    private boolean mShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        // Setup ReviewAsyncTask
        setUpReviewAsyncTask();

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.business_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup all views
        ((TextView) findViewById(R.id.business_name)).setText(getIntent().getStringExtra(bName));
        ((TextView) findViewById(R.id.business_address)).setText(getIntent().getStringExtra(bAddr));
        ((TextView) findViewById(R.id.business_category_text)).setText(getIntent().getStringExtra(bCat));
        ((TextView) findViewById(R.id.business_phone)).setText(getIntent().getStringExtra(bPhone));
        ((TextView) findViewById(R.id.business_dist)).setText(Math.round(getIntent().getDoubleExtra(bDist, 0))+" km");
        ((RatingBar) findViewById(R.id.business_rating)).setRating(getIntent().getFloatExtra(bRating, 0));
//        ((TextView) findViewById(R.id.business_snippet)).setText(getIntent().getStringExtra(bSnippet));

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
        final View maskView = (View) findViewById(R.id.business_bottom_mask);

        final double scrollMax = 600;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = scrollView.getScrollY();

                        if (scrollY <= scrollMax) {
                            maskView.setAlpha(Math.min((float) (scrollY / (scrollMax - 10)), (float) 1.0));
                        }
                        else {
                            maskView.setAlpha(1);
                        }
                        Log.i("scroll-anim", "(scrollY, maskAlpha): ("+scrollY+", "+maskView.getAlpha()+")");

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

    @Override
    public void onPostExecute(JSONArray jsonArray) {

        if (jsonArray.length()==0) {
            mListView = (ListView) findViewById(R.id.review_list);
            ((ViewGroup)mListView.getParent().getParent()).removeView((View)mListView.getParent());
            return;
        }

        //Parse jsonArray
        JSONObject review;
        float rating;
        String date;
        String content;
        String userName;
        String userImgUrl;

        try {
            for (int i=0; i<jsonArray.length(); i++) {

                review = jsonArray.getJSONObject(i);

                rating = review.has("rating")
                        ? (float)review.getDouble("rating")
                        : (float)0.0;

                date = review.has("date")
                        ? review.getString("date")
                        : null;

                content = review.has("review_content")
                        ? review.getString("review_content")
                        : null;

                userName = review.has("user_name")
                        ? review.getString("user_name")
                        : "Anonymous";

                userImgUrl = review.has("user_img_url")
                        ? review.getString("user_img_url")
                        : null;

                mReviewList.add(new ReviewItem(rating, date, content, userName, userImgUrl));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mReviewListAdapter = new ReviewListBaseAdapter(this, mReviewList);
        mListView = (ListView) findViewById(R.id.review_list);
        mListView.setEmptyView(findViewById(R.id.empty_list));
        mListView.setAdapter(mReviewListAdapter);
        setUpListViewHeight();

    }

    private void setUpListViewHeight() {

        // Get total height of all items.
        int totalItemsHeight = mListView.getPaddingTop() + mListView.getPaddingBottom();
        for (int i=0; i<mListView.getAdapter().getCount(); i++) {
            View item = mListView.getAdapter().getView(i, null, mListView);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) (mListView.getParent())).getWidth(), View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            item.measure(widthMeasureSpec, heightMeasureSpec);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = mListView.getDividerHeight() *
                (mListView.getAdapter().getCount() - 1);

        // Set list height.
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        mListView.setEnabled(false);
        mListView.setLayoutParams(params);
        mListView.requestLayout();
        mListView.setEnabled(true);

    }

    private void setUpReviewAsyncTask() {

        ReviewAsyncTask mReviewAsyncTask = new ReviewAsyncTask();
        mReviewAsyncTask.setResponseDelegate(this);
        mReviewAsyncTask.execute(getIntent().getStringExtra(BusinessActivity.bId));

    }

    public void onCall(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra(bPhone)));
        startActivity(callIntent);

    }

    public void onNavigate(View view) {

        Log.i("on-navigate", getIntent().getStringExtra(bLatLng));
        Uri intentUri = Uri.parse("google.navigation:q="+getIntent().getStringExtra(bLatLng));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

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

}

