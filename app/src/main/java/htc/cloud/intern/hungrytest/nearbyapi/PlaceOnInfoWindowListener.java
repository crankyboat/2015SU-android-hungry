package htc.cloud.intern.hungrytest.nearbyapi;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import htc.cloud.intern.hungrytest.business.BusinessActivity;

/**
 * Created by intern on 8/5/15.
 */
public class PlaceOnInfoWindowListener implements GoogleMap.OnInfoWindowClickListener {

    private Activity mActivity;

    public PlaceOnInfoWindowListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        mActivity.startActivity(new Intent(mActivity, BusinessActivity.class));
    }

}
