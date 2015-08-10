package htc.cloud.intern.hungrytest.business;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        // Update all views
        Intent mIntent = getIntent();
//        ((TextView) findViewById(R.id.business_name)).setText(getIntent().getStringExtra(bName));
        ((TextView) findViewById(R.id.business_address)).setText(getIntent().getStringExtra(bAddr));
        ((TextView) findViewById(R.id.business_category_text)).setText(getIntent().getStringExtra(bCat));
//        ((TextView) findViewById(R.id.business_category)).setText(getIntent().getStringExtra(bCat));
//        ((TextView) findViewById(R.id.business_phone)).setText(getIntent().getStringExtra(bPhone));
//        ((RatingBar) findViewById(R.id.business_rating)).setRating(getIntent().getFloatExtra(bRating, 0));
//        ((RatingBar) findViewById(R.id.business_dist)).setRating(getIntent().getFloatExtra(bDist, 0));
//        ((TextView) findViewById(R.id.business_snippet)).setText(getIntent().getStringExtra(bSnippet));

        Ion.with((ImageView)findViewById(R.id.business_image))
                .placeholder(R.drawable.ic_stars_black_24dp)
                .load(getIntent().getStringExtra(bImgSrc).replace("ls.jpg", "o.jpg"));

//        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra(bPhone)));
//                startActivity(callIntent);
//            }
//        });

        final View scrollView = (View) findViewById(R.id.transparent_padding).getParent().getParent();
        final View transparentView = (View) findViewById(R.id.transparent_padding);
        final double scrollMax = 660;
        scrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = scrollView.getScrollY();
                        if (scrollY <= scrollMax) {
                            transparentView.setAlpha((float)(scrollY/scrollMax));
                        }
                        Log.i("BUSINESS-FRAGMENT", "ScrollY: " + scrollY);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
