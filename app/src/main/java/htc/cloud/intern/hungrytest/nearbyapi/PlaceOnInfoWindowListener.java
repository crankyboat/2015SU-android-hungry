package htc.cloud.intern.hungrytest.nearbyapi;

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

//        mActivity.startActivity(new Intent(mActivity, BusinessActivity.class));

        Intent businessIntent = new Intent(mActivity, BusinessActivity.class);

        businessIntent.putExtra(BusinessActivity.bName, mMarkerInfo.get(marker).getName());
        businessIntent.putExtra(BusinessActivity.bAddr, mMarkerInfo.get(marker).getAddr());
        businessIntent.putExtra(BusinessActivity.bCat, mMarkerInfo.get(marker).getCategory());
        businessIntent.putExtra(BusinessActivity.bPhone, mMarkerInfo.get(marker).getPhoneNum());
        businessIntent.putExtra(BusinessActivity.bRating, (float)4.0);
        businessIntent.putExtra(BusinessActivity.bDist, mMarkerInfo.get(marker).getDist());
        businessIntent.putExtra(BusinessActivity.bSnippet, mMarkerInfo.get(marker).getSnippet());
        businessIntent.putExtra(BusinessActivity.bImgSrc, mMarkerInfo.get(marker).getImgSrc());
        businessIntent.putExtra(BusinessActivity.bImgList, mMarkerInfo.get(marker).getImgList());

        mActivity.startActivity(businessIntent);
    }

}
