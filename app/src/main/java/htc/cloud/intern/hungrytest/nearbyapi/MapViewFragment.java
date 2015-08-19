package htc.cloud.intern.hungrytest.nearbyapi;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final LatLng TAIPEI_LAT_LNG = new LatLng(25.0, 121.6);
    public GoogleMap mMap;
    public MapFragment mMapFragment;
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

        // TODO
        FloatingActionButton reloadButton = (FloatingActionButton) getActivity().findViewById(R.id.map_reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
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
            mCurrentMarker = mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
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

        for (PlaceState place : likelyPlaces) {
            Log.i("location-mapview", String.format("Place '%s'", place.getName()));
            String placeName = place.getName().toString();

            LatLng placeLatLng = place.getLatLng();
            Marker placeMarker = mMap.addMarker(new MarkerOptions()
                    .position(placeLatLng)
                    .title(placeName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
//        Log.i("location-mapview", String.format("(%f, %f, %f, %f), Zoom %f", min_lat, max_lat, min_lon, max_lon, zoom));

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));
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
//                Log.i("location-mapview", "NewZoom "+zoom);
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