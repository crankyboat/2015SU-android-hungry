package htc.cloud.intern.hungrytest.nearbyapi;

/**
 * Created by intern on 7/24/15.
 */

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.UserState;
import htc.cloud.intern.hungrytest.hungryapi.AsyncResponse;
import htc.cloud.intern.hungrytest.hungryapi.FeedbackAsyncTask;
import htc.cloud.intern.hungrytest.hungryapi.HungryAsyncTask;

public class MapFragment extends Fragment implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener, AsyncResponse {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageButton mSwitchviewButton;
    private boolean isMapView;

    protected static final String TAG = "location-test";
    protected static int PLACE_PICKER_REQUEST = 1;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LatLng mCurrentLocation;
    private Activity mActivity;

    protected MapViewFragment mMapViewFragment;
    protected MapListViewFragment mMapListViewFragment;

    public static MapFragment newInstance(int sectionNumber) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        isMapView = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        setUpViewFragments();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
        setUpGoogleApiClient();
        setUpLocationRequest();
//        setUpHungryApiAsyncTasks();

        ActionBar actionBar = ((ActionBarActivity) activity).getSupportActionBar();
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_map_mapview_toolbar, null);

        if (actionBar != null) {
            actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM |
                ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE |
                ActionBar.DISPLAY_HOME_AS_UP);
            actionBar.setCustomView(view, new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL));
        }


        mSwitchviewButton = (ImageButton) getActivity().findViewById(R.id.action_switchview);
        mSwitchviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwitchViewClicked(view);
            }
        });

    }

    @Override
    public void onDetach (){
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(
                    ActionBar.DISPLAY_SHOW_HOME |
                            ActionBar.DISPLAY_SHOW_TITLE |
                            ActionBar.DISPLAY_HOME_AS_UP);
        }
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(getActivity(), "Google Play Service Missing.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        UserState us = ((MainActivity) mActivity).mUserState;
        us.setUserLocation(mCurrentLocation);
        setUpHungryApiAsyncTasks();

    }

    @Override
    public void onPostExecute(JSONArray jsonArray) {
        try {
            ArrayList<PlaceState> likelyPlaces = new ArrayList<PlaceState>();

            double maxDistance = 0.0;
            JSONObject business;
            String id;
            String name;
            String address;
            double distance;
            LatLng latlng;
            String imgSrc;
            String category;
            String phoneNum;
            String snippet;
            ArrayList<String> imgList;

            for (int i=0; i<jsonArray.length(); i++) {
                business = jsonArray.getJSONObject(i);

                id = business.getString("business_id");//.replaceFirst("-(.*)", "");

                name = business.getString("name");

                latlng = new LatLng(business.getJSONObject("location").getJSONObject("coordinate").getDouble("latitude"),
                        business.getJSONObject("location").getJSONObject("coordinate").getDouble("longitude"));


                imgSrc = business.has("image_url")
                        ? business.getString("image_url").replace("ms.jpg", "ls.jpg")
                        : "";

                phoneNum = business.has("display_phone")
                        ? business.getString("display_phone")
                        : "Phone Number N/A";

                snippet = business.has("snippet_text")
                        ? business.getString("snippet_text")
                        : "";

                distance = business.has("distance")
                        ? business.getDouble("distance")
                        : -1.0;

                maxDistance = (distance > maxDistance)
                        ? distance
                        : maxDistance;

                int maxCatCount = 4;
                category = new String();
                if (business.has("categories")) {
                    for (int j = 0; j < Math.min(business.getJSONArray("categories").getJSONArray(0).length(), maxCatCount); j++) {
                        category += business.getJSONArray("categories").getJSONArray(0).get(j)+" ";
                    }
                }

                address = new String();
                if (business.has("location") && business.getJSONObject("location").has("display_address")) {
                    for (int j = 0; j < business.getJSONObject("location").getJSONArray("display_address").length(); j++) {
                        address += business.getJSONObject("location").getJSONArray("display_address").get(j)+" ";
                    }
                }

                imgList = new ArrayList<String>();
                for (int j = 0; j < business.getJSONArray("img_urls_and_descs").length(); j++) {
                    imgList.add(business.getJSONArray("img_urls_and_descs").getJSONArray(j).getString(0));
                }

                likelyPlaces.add(new PlaceState(id, name, address, distance, latlng, imgSrc, category, phoneNum, snippet, imgList));
            }

            Log.i("hungry-api", "Parsing Completed.");

            if (mMapViewFragment != null) {
                mMapViewFragment.onLocationChanged(mCurrentLocation, likelyPlaces, maxDistance);
            }
            if (mMapListViewFragment != null) {
                mMapListViewFragment.onLocationChanged(likelyPlaces);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, mActivity);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(mActivity, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void setUpPlacePicker() {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        Context context = mActivity.getApplicationContext();
//        try {
//            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }

    protected synchronized void setUpGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    protected void setUpLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120*1000); //ms
        mLocationRequest.setFastestInterval(120*1000);  //ms
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void setUpHungryApiAsyncTasks() {

        // Dummy user state
        UserState us = ((MainActivity)mActivity).mUserState;
        us.setFeedback("business1", 1);
        us.setFeedback("business2", 100);

        HungryAsyncTask mHungryAsyncTask = new HungryAsyncTask();
        mHungryAsyncTask.setResponseDelegate(this);
        mHungryAsyncTask.execute(us);

//        FeedbackAsyncTask mFeedbackAsyncTask = new FeedbackAsyncTask();
//        mFeedbackAsyncTask.execute(us);
    }

    private void setUpViewFragments() {

        mMapViewFragment = MapViewFragment.newInstance(0);
        mMapListViewFragment = MapListViewFragment.newInstance(0);
        getFragmentManager().beginTransaction()
                .add(R.id.map_container, mMapViewFragment)
                .add(R.id.map_container, mMapListViewFragment)
                .hide(mMapListViewFragment)
                .commit();

    }

    public void onSwitchViewClicked(View view){

        mSwitchviewButton.setImageResource(isMapView ? R.drawable.ic_map_black_24dp : R.drawable.ic_list_black_24dp);

        Fragment switchFragment = (isMapView)
                ? mMapListViewFragment
                : mMapViewFragment;
        Fragment switchoutFragment = (isMapView)
                ? mMapViewFragment
                : mMapListViewFragment;

        getFragmentManager().beginTransaction()
                .hide(switchoutFragment)
                .show(switchFragment)
                .commit();

        isMapView = !isMapView;
//        Toast.makeText(mActivity, "switchFragment = "+switchFragment.toString(), Toast.LENGTH_LONG).show();

    }


}