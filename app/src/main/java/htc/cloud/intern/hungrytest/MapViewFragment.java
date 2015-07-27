package htc.cloud.intern.hungrytest;

/**
 * Created by intern on 7/24/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;


public class MapViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private GoogleMap mMap;

    public static MapViewFragment newInstance(int sectionNumber) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapViewFragment() {
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    public void setUpMap(GoogleMap map){
        mMap = map;
        mMap.setMyLocationEnabled(true);
    }

}