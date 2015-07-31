package htc.cloud.intern.hungrytest;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by intern on 7/31/15.
 */
public class PlaceState {

    private String mName;
    private LatLng mLatLng;

    public PlaceState(String name, LatLng latlng) {
        mName = name;
        mLatLng = latlng;
    }

    public String getName() {
        return mName;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

}
