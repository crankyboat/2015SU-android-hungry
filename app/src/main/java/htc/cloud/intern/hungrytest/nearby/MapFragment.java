package htc.cloud.intern.hungrytest.nearby;

/**
 * Created by intern on 7/24/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.UserState;
import htc.cloud.intern.hungrytest.hungryapi.AsyncResponse;
import htc.cloud.intern.hungrytest.hungryapi.HungryAsyncTask;

public class MapFragment extends Fragment implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener, AsyncResponse {

    protected static final String TAG = "map-fragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int MAX_ZOOM_LEVEL = 9;
    private static final int MAX_NUM_PLACES = 20;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LatLng mCurrentLocation;
    private Activity mActivity;
    private HashMap<Integer, ArrayList<PlaceState>> mCachedPlacesByZoom;

    private boolean isMapView;
    protected MapViewFragment mMapViewFragment;
    protected MapListViewFragment mMapListViewFragment;
    private FloatingActionButton mReloadButton;
    private ImageButton mSwitchviewButton;
    private ProgressDialog mProgressDialog;

    public static MapFragment newInstance(int sectionNumber) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
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
        Log.i(TAG, "onAttach()");
        super.onAttach(activity);

        mActivity = activity;
        setUpGoogleApiClient();
        setUpLocationRequest();

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

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDetach (){
        Log.i(TAG, "onDetach()");

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(
                    ActionBar.DISPLAY_SHOW_HOME |
                            ActionBar.DISPLAY_SHOW_TITLE |
                            ActionBar.DISPLAY_HOME_AS_UP);
        }

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onDetach();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected()");

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended()");

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

        mCachedPlacesByZoom = new HashMap<Integer, ArrayList<PlaceState>>();
        for (int i=0; i<=MAX_ZOOM_LEVEL; i+=2) {    // increment by two levels
            setUpHungryApiAsyncTask(i);
        }

        mProgressDialog = new CustomProgressDialog(mActivity);
        mProgressDialog.show();

    }

    @Override
    public void onPostExecute(AsyncTask<?, ?, ?> asyncTask, ArrayList<?> placeList) {

        ArrayList<PlaceState> rankedPlaceList;
        int currentZoom = ((HungryAsyncTask)asyncTask).getCurrentZoom();
        if (currentZoom==0) {

            mProgressDialog.dismiss();

            rankedPlaceList = new ArrayList<PlaceState>((ArrayList<PlaceState>)placeList);
            Collections.sort(rankedPlaceList);
            int maxIndex = Math.min(MAX_NUM_PLACES, placeList.size());
            rankedPlaceList = new ArrayList<PlaceState>(rankedPlaceList.subList(0, maxIndex));

            if (mMapViewFragment != null) {
                mMapViewFragment.mCurrentMarker = null;
                mMapViewFragment.mMap.clear();
                mMapViewFragment.onLocationChanged(mCurrentLocation, rankedPlaceList);
            }

            if (mMapListViewFragment != null) {
                mMapListViewFragment.onLocationChanged(rankedPlaceList);
            }
        }

        mCachedPlacesByZoom.put(currentZoom, new ArrayList<PlaceState>((ArrayList<PlaceState>) placeList));
        Log.i("hungry-api", "(zoom, count): (" + currentZoom + ", " + mCachedPlacesByZoom.get(currentZoom).size() + ")");

    }

    @Override
    public void onNoExecute(AsyncTask<?, ?, ?> asyncTask, String message) {

        int currentZoom = ((HungryAsyncTask)asyncTask).getCurrentZoom();
        if (currentZoom==0) {
            mProgressDialog.dismiss();
            Toast.makeText(mActivity, "Network error. ", Toast.LENGTH_LONG).show();
        }

        // TODO
        // Pause for awhile then reconnect

    }

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
        mLocationRequest.setInterval(3*60*1000); //ms
        mLocationRequest.setFastestInterval(60*1000);  //ms
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void setUpHungryApiAsyncTask(int range) {

        UserState us = ((MainActivity)mActivity).mUserState;
        HungryAsyncTask mHungryAsyncTask = new HungryAsyncTask(this, range);
        mHungryAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, us);

    }

    private void setUpViewFragments() {

        isMapView = true;
        mMapViewFragment = MapViewFragment.newInstance(0);
        mMapViewFragment.setReloadDelegate(this);
        mMapListViewFragment = MapListViewFragment.newInstance(0);
        getFragmentManager().beginTransaction()
                .add(R.id.map_container, mMapViewFragment)
                .add(R.id.map_container, mMapListViewFragment)
                .hide(mMapListViewFragment)
                .commit();

    }

    public void onSwitchViewClicked(View view){

        mReloadButton = (FloatingActionButton) getActivity().findViewById(R.id.map_reload);
        mReloadButton.setVisibility(isMapView ? View.GONE : View.VISIBLE);
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

    }

    public void onReload(float mapZoom) {

        if (mCachedPlacesByZoom==null || mCachedPlacesByZoom.size()==0) {
            Toast.makeText(mActivity, "Network error.", Toast.LENGTH_LONG).show();
            return;
        }

        // Use the appropriate ArrayLists depending on Zoom Level
        ArrayList<PlaceState> rankedPlaceList;
        Log.i("hungry-api", "mapZoom: "+mapZoom);
        if (mapZoom < 11.4 && mCachedPlacesByZoom.get(9)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(9));
        }
        else if (mapZoom < 11.4 && mCachedPlacesByZoom.get(8)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(8));
        }
        else if (mapZoom < 11.6 && mCachedPlacesByZoom.get(7)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(7));
        }
        else if (mapZoom < 11.9 && mCachedPlacesByZoom.get(6)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(6));
        }
        else if (mapZoom < 12.4 && mCachedPlacesByZoom.get(5)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(5));
        }
        else if (mapZoom < 12.9 && mCachedPlacesByZoom.get(4)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(4));
        }
        else if (mapZoom < 13.2 && mCachedPlacesByZoom.get(3)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(3));
        }
        else if (mapZoom < 14.0 && mCachedPlacesByZoom.get(2)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(2));
        }
        else if (mapZoom < 14.7 && mCachedPlacesByZoom.get(1)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(1));
        }
        else if (mCachedPlacesByZoom.get(0)!=null) {
            rankedPlaceList = new ArrayList<PlaceState>(mCachedPlacesByZoom.get(0));
        }
        else {
            rankedPlaceList = new ArrayList<PlaceState>();
        }

        // Get top 20 ranked
        Collections.sort(rankedPlaceList);
        int maxIndex = Math.min(MAX_NUM_PLACES, rankedPlaceList.size());
        rankedPlaceList = new ArrayList<PlaceState>(rankedPlaceList.subList(0, maxIndex));

        if (mMapViewFragment != null) {
            mMapViewFragment.mMap.clear();
            mMapViewFragment.mCurrentMarker = null;
            mMapViewFragment.onLocationChanged(mCurrentLocation, (ArrayList<PlaceState>)rankedPlaceList);
        }
        if (mMapListViewFragment != null) {
            mMapListViewFragment.onLocationChanged((ArrayList<PlaceState>)rankedPlaceList);
        }

    }

    public ArrayList<PlaceState> getCachedPlaces() {

        for (int i = MAX_ZOOM_LEVEL; i >= 0; i--) {
            if (mCachedPlacesByZoom!=null && mCachedPlacesByZoom.get(i) != null) {
                return mCachedPlacesByZoom.get(i);
            }
        }
        return null;

    }

}