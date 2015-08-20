package htc.cloud.intern.hungrytest.nearbyapi;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.HashMap;

import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/28/15.
 */
public class MapViewFragment extends Fragment {

    private static final String TAG = "map-view";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final LatLng TAIPEI_LAT_LNG = new LatLng(25.0, 121.6);
    private static HashMap<Integer, Integer> mMarkerRes = new HashMap<Integer, Integer>();

    public GoogleMap mMap;
    public MapFragment mMapFragment;
    public FloatingActionButton mReloadButton;
    protected LatLng mCurrentLocation;
    protected Marker mCurrentMarker;
    protected HashMap<Marker, PlaceState> mMarkerInfo = new HashMap<Marker, PlaceState>();

    public static MapViewFragment newInstance(int sectionNumber) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapViewFragment() {
        mMarkerRes.put(1, R.drawable.number_1);
        mMarkerRes.put(2, R.drawable.number_2);
        mMarkerRes.put(3, R.drawable.number_3);
        mMarkerRes.put(4, R.drawable.number_4);
        mMarkerRes.put(5, R.drawable.number_5);
        mMarkerRes.put(6, R.drawable.number_6);
        mMarkerRes.put(7, R.drawable.number_7);
        mMarkerRes.put(8, R.drawable.number_8);
        mMarkerRes.put(9, R.drawable.number_9);
        mMarkerRes.put(10, R.drawable.number_10);
        mMarkerRes.put(11, R.drawable.number_11);
        mMarkerRes.put(12, R.drawable.number_12);
        mMarkerRes.put(13, R.drawable.number_13);
        mMarkerRes.put(14, R.drawable.number_14);
        mMarkerRes.put(15, R.drawable.number_15);
        mMarkerRes.put(16, R.drawable.number_16);
        mMarkerRes.put(17, R.drawable.number_17);
        mMarkerRes.put(18, R.drawable.number_18);
        mMarkerRes.put(19, R.drawable.number_19);
        mMarkerRes.put(20, R.drawable.number_20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_mapview, container, false);
        mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TAIPEI_LAT_LNG, 10));
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.i("map-zoom", "zoom: "+cameraPosition.zoom);
            }
        });

        mReloadButton = (FloatingActionButton) getActivity().findViewById(R.id.map_reload);
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapFragment.onReload(mMap.getCameraPosition().zoom);
            }
        });

        return rootView;

    }

    public void setReloadDelegate(MapFragment mapFragment) {
        mMapFragment = mapFragment;
    }

    public void onLocationChanged(LatLng location, ArrayList<PlaceState> likelyPlaces){

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        mCurrentLocation = location;
        if (mCurrentMarker == null) {
            mCurrentMarker = mMap.addMarker(new MarkerOptions()
                    .position(mCurrentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.here)));
            mMarkerInfo.put(mCurrentMarker, null);
        }
        else {
            mCurrentMarker.setPosition(mCurrentLocation);
        }
        boundsBuilder.include(mCurrentLocation);

//        double min_lat = 1000;
//        double min_lon = 1000;
//        double max_lat = -1;
//        double max_lon = -1;

//        for (PlaceState place : likelyPlaces) {
        for (int i=0; i<likelyPlaces.size(); i++) {
            PlaceState place = likelyPlaces.get(i);
            Log.i(TAG, String.format("Place '%s'", place.getName()));
            String placeName = place.getName().toString();

            LatLng placeLatLng = place.getLatLng();
            Marker placeMarker = mMap.addMarker(new MarkerOptions()
                    .position(placeLatLng)
                    .title(placeName)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    .icon(BitmapDescriptorFactory.fromResource(mMarkerRes.get(i+1))));

            mMarkerInfo.put(placeMarker, place);
            boundsBuilder.include(placeLatLng);

//            min_lat = placeLatLng.latitude < min_lat ? placeLatLng.latitude : min_lat;
//            min_lon = placeLatLng.longitude < min_lon ? placeLatLng.longitude : min_lon;
//            max_lat = placeLatLng.latitude > max_lat ? placeLatLng.latitude : max_lat;
//            max_lon = placeLatLng.longitude > max_lon ? placeLatLng.longitude : max_lon;

        }
        mMap.setInfoWindowAdapter(new PlaceInfoWindowAdapter(getActivity(), mMarkerInfo));
        mMap.setOnInfoWindowClickListener(new PlaceOnInfoWindowListener(getActivity(), mMarkerInfo));

        // Adjust Zoom
//        double degToRad = 57.2958;
//        double meanRadiusEarth = 6371;
//        int displaySize = getChildFragmentManager().findFragmentById(R.id.map).getView().getWidth();
//        double dist = (meanRadiusEarth * Math.acos(Math.sin(min_lat / degToRad) * Math.sin(max_lat / degToRad) +
//                (Math.cos(min_lat / degToRad) * Math.cos(max_lat / degToRad) * Math.cos((max_lon / degToRad) - (min_lon / degToRad)))));
//        double zoom = mMarkerInfo.size() > 1
//                ? Math.floor(8 - Math.log(1.6446 * dist / Math.sqrt(2 * (displaySize * displaySize))) / Math.log(2)) - 1
//                : 15;
//        Log.i(TAG, String.format("(%f, %f, %f, %f), Zoom %f", min_lat, max_lat, min_lon, max_lon, zoom));

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, (float) zoom));
//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                VisibleRegion vr = mMap.getProjection().getVisibleRegion();
//                double min_lon = vr.latLngBounds.southwest.longitude;
//                double max_lat = vr.latLngBounds.northeast.latitude;
//                double max_lon = vr.latLngBounds.northeast.longitude;
//                double min_lat = vr.latLngBounds.southwest.latitude;
//
//                // Adjust Zoom
//                double degToRad = 57.2958;
//                double meanRadiusEarth = 6371;
//                int displaySize = getChildFragmentManager().findFragmentById(R.id.map).getView().getWidth();
//
//                double dist = (meanRadiusEarth * Math.acos(Math.sin(min_lat / degToRad) * Math.sin(max_lat / degToRad) +
//                        (Math.cos(min_lat / degToRad) * Math.cos(max_lat / degToRad) * Math.cos((max_lon / degToRad) - (min_lon / degToRad)))));
//                double zoom = mMarkerInfo.size() > 1
//                        ? Math.floor(8 - Math.log(1.6446 * dist / Math.sqrt(2 * (displaySize * displaySize))) / Math.log (2))
//                        : 15;
//
//                Log.i(TAG, "NewZoom "+zoom);
//            }
//        });

    }

//    @Override
//    public void onStop(){
//        Toast.makeText(getActivity(), "MapViewFragment onStop().", Toast.LENGTH_LONG).show();
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroyView(){
//        Toast.makeText(getActivity(), "MapViewFragment onDestroyView().", Toast.LENGTH_LONG).show();
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        Toast.makeText(getActivity(), "MapViewFragment onResume().", Toast.LENGTH_LONG).show();
//
//    }

}