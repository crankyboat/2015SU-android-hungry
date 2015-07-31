package htc.cloud.intern.hungrytest.nearby;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/28/15.
 */
public class MapListViewFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<ListData> mList = new ArrayList<ListData>();
    private ListView mListView;

    public static MapListViewFragment newInstance(int sectionNumber) {
        MapListViewFragment fragment = new MapListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_listview, container, false);

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mListView.setEmptyView(rootView.findViewById(android.R.id.empty));
        setListAdapter(new ListBaseAdapter(getActivity(), mList));

        return rootView;
    }

    public void onLocationChanged(PlaceLikelihoodBuffer likelyPlaces){

        mList = new ArrayList<ListData>();
        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//            Log.i("location-maplistview", String.format("Place '%s' has likelihood: %g",
//                    placeLikelihood.getPlace().getName(),
//                    placeLikelihood.getLikelihood()));
            String placeName = placeLikelihood.getPlace().getName().toString();
            LatLng placeLatLng = placeLikelihood.getPlace().getLatLng();
            mList.add(new ListData(placeName, placeLatLng.toString(), R.drawable.ic_stars_black_24dp));
        }
        setListAdapter(new ListBaseAdapter(getActivity(), mList));

    }

}
