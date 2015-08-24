package htc.cloud.intern.hungrytest;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by intern on 7/30/15.
 */
public class UserState {

    public static final String TAG = "user-state";

//    private final TelephonyManager mTelephonyManager;
    public final String mDeviceID;
    public String mCurrentLocation;
    public HashMap<String, Integer> mFeedback;

    public UserState(Context context) {
//        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        mDeviceID = mTelephonyManager.getDeviceId();
        mDeviceID = "ipPukGoxTOYn-Wd1WGBv-A";
        mCurrentLocation = "0,0";
        mFeedback = new HashMap<String, Integer>();
    }

    public void setUserLocation(LatLng location) {
        mCurrentLocation = location.latitude+","+location.longitude;
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
            feedbackString = feedbackString + "feedbacks=" + pair.getKey() + "," + pair.getValue() +"&";
            iter.remove(); // avoids a ConcurrentModificationException
        }
        feedbackString = feedbackString.length()>0
                ? feedbackString.substring(0, feedbackString.length()-1)
                : feedbackString;

        mFeedback = new HashMap<String, Integer>();

        Log.i(TAG, "feedback string: "+feedbackString);
        return feedbackString;

    }

}
