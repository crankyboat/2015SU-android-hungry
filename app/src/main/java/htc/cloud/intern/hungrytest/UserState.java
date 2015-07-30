package htc.cloud.intern.hungrytest;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by intern on 7/30/15.
 */
public class UserState {

    private final TelephonyManager mTelephonyManager;
    public final String mDeviceID;
    public String mCurrentLocation;

    public UserState(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        mDeviceID = mTelephonyManager.getDeviceId();
        mDeviceID = "124";
    }


    public void setUserLocation(LatLng location) {
//        mCurrentLocation = location.latitude+","+location.longitude;
        mCurrentLocation = "121.0,23.3";
    }

}
