package htc.cloud.intern.hungrytest.nearby;

/**
 * Created by intern on 7/24/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import htc.cloud.intern.hungrytest.R;


public class MapFragment extends Fragment implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageButton mSwitchviewButton;
    private boolean isMapView;

    protected static final String TAG = "location-test";
    protected static int PLACE_PICKER_REQUEST = 1;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LatLng mCurrentLocation;
    protected Marker mCurrentMarker;
    private Activity mActivity;
    protected GoogleMap mMap;

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
        isMapView = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        onSwitchViewClicked(null);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
        setUpGoogleApiClient();
        setUpLocationRequest();

//        setUpMapIfNeeded();
//        setUpPlacePicker();
//        getLocalPlaces();

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

    public void onSwitchViewClicked(View view){

        mSwitchviewButton.setImageResource(isMapView ? R.drawable.ic_map_black_24dp : R.drawable.ic_list_black_24dp);

        Fragment switchFragment;
        if (isMapView) {
            switchFragment = (mMapListViewFragment == null)
                    ? (ListFragment)MapListViewFragment.newInstance(0)
                    : mMapListViewFragment;
            mMapListViewFragment = (MapListViewFragment)switchFragment;
        }
        else {
            switchFragment = (mMapViewFragment == null)
                    ? MapViewFragment.newInstance(0)
                    : mMapViewFragment;
            mMapViewFragment = (MapViewFragment)switchFragment;

        }
        getFragmentManager().beginTransaction()
                .replace(R.id.map_container, switchFragment)
                .commit();

        isMapView = !isMapView;

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
        if (!mGoogleApiClient.isConnected() && mCurrentMarker != null) {
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
    public void onLocationChanged(Location location) {

        if (mMapViewFragment != null) {
            mMapViewFragment.onLocationChanged(location, mGoogleApiClient);
        }
        if (mMapListViewFragment != null) {
            mMapListViewFragment.onLocationChanged(location, mGoogleApiClient);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(getActivity(), "Google Play Service Missing.", Toast.LENGTH_LONG).show();
        }
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
        mLocationRequest.setInterval(10000); //ms
        mLocationRequest.setFastestInterval(1000);  //ms
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setUpPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Context context = mActivity.getApplicationContext();
        try {
            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(mActivity, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getLocalPlaces() {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));

                    // Display each place on the map.
                    String placeName = placeLikelihood.getPlace().getName().toString();
                    LatLng placeLatLng = placeLikelihood.getPlace().getLatLng();
                    mMap.addMarker(new MarkerOptions()
                            .position(placeLatLng)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 17));

                }
                likelyPlaces.release();
            }
        });

    }


}