package htc.cloud.intern.hungrytest.nearby;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.business.BusinessActivity;

/**
 * Created by intern on 8/5/15.
 */
public class PlaceOnInfoWindowListener implements GoogleMap.OnInfoWindowClickListener {

    private Activity mActivity;
    private HashMap<Marker, PlaceState> mMarkerInfo;

    public PlaceOnInfoWindowListener(Activity activity, HashMap<Marker, PlaceState> markerInfo) {
        mActivity = activity;
        mMarkerInfo = markerInfo;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        PlaceState business = mMarkerInfo.get(marker);
        Intent businessIntent = BusinessActivity.setUpBusinessIntent(mActivity, business);
        mActivity.startActivity(businessIntent);

    }

}
