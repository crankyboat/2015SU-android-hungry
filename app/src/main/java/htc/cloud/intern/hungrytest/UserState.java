package htc.cloud.intern.hungrytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by intern on 7/30/15.
 */
public class UserState {

    private final TelephonyManager mTelephonyManager;
    public final String mDeviceID;
    public String mCurrentLocation;
    public HashMap<String, Integer> mFeedback;

    public UserState(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        mDeviceID = mTelephonyManager.getDeviceId();
        mDeviceID = "ipPukGoxTOYn-Wd1WGBv-A";
        mCurrentLocation = "24.9837193%2C121.5427091";
        mFeedback = new HashMap<String, Integer>();
    }


    public void setUserLocation(LatLng location) {
//        mCurrentLocation = location.latitude+","+location.longitude;
        mCurrentLocation = "24.9837193%2C121.5427091";
    }

    public void setFeedback(String key, Integer value) {

        if (mFeedback.containsKey(key)) {
            Integer prevValue = mFeedback.get(key);
            mFeedback.put(key, prevValue+value);
        }
        else {
            mFeedback.put(key, value);
        }

    }

    public String getFeedbackAndClear() {

        String feedbackString = new String();

        Iterator iter = mFeedback.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry)iter.next();
            feedbackString = feedbackString + pair.getKey() + ":" + pair.getValue() +",";
            iter.remove(); // avoids a ConcurrentModificationException
        }
        feedbackString = feedbackString.length()>0
                ? feedbackString.substring(0, feedbackString.length()-1)
                : feedbackString;

        mFeedback = new HashMap<String, Integer>();

        return feedbackString;

    }

}
