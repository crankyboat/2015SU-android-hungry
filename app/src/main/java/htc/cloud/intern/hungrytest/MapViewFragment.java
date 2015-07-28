package htc.cloud.intern.hungrytest;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by intern on 7/28/15.
 */
public class MapViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private GoogleMap mMap;
    protected LatLng mCurrentLocation;
    protected Marker mCurrentMarker;

    public static MapViewFragment newInstance(int sectionNumber) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapViewFragment() {
    }

    public GoogleMap getMap(){
        return mMap;
    }

    public void onLocationChanged(Location location, GoogleApiClient mGoogleApiClient){

        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mCurrentMarker != null) {
            mMap.clear();
//            mCurrentMarker = mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
        }
//        else {
//            mCurrentMarker.setPosition(mCurrentLocation);
//        }
        mCurrentMarker = mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
        mCurrentMarker.setTitle("My Location");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 17));

        getLocalPlaces(mGoogleApiClient);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_mapview, container, false);
        mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        return rootView;
    }

    private void getLocalPlaces(GoogleApiClient mGoogleApiClient) {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("location-test", String.format("Place '%s' has likelihood: %g",
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