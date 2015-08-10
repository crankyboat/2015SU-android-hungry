package htc.cloud.intern.hungrytest.nearbyapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.business.BusinessActivity;

/**
 * Created by intern on 7/28/15.
 */
public class MapListViewFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<PlaceState> mList = new ArrayList<PlaceState>();
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

    public void onLocationChanged(ArrayList<PlaceState> likelyPlaces){

        mList = new ArrayList<PlaceState>();
        for (PlaceState place : likelyPlaces) {

            Log.i("location-maplistview", String.format("Place '%s'", place.getName()));
            String placeName = place.getName().toString();
            String placeAddr = place.getAddr();
            LatLng placeLatLng = place.getLatLng();
            String placeImgSrc = place.getImgSrc();
            String placePhoneNum = place.getPhoneNum();
            String placeCategory = place.getCategory();
            String placeSnippet = place.getSnippet();

            mList.add(new PlaceState(place.getId(), placeName, placeAddr, placeLatLng,
                    placeImgSrc, placeCategory, placePhoneNum, placeSnippet));

        }
        setListAdapter(new ListBaseAdapter(getActivity(), mList));

    }

    @Override
    public void onListItemClick (ListView listView, View view, int position, long id) {

//        Toast.makeText(getActivity(), "("+position+", "+id+")", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getActivity(), BusinessActivity.class));

        PlaceState item = (PlaceState) getListView().getItemAtPosition(position);
        Intent businessIntent = new Intent(getActivity(), BusinessActivity.class);

        businessIntent.putExtra(BusinessActivity.bName, item.getName());
        businessIntent.putExtra(BusinessActivity.bAddr, item.getAddr());
        businessIntent.putExtra(BusinessActivity.bCat, item.getCategory());
        businessIntent.putExtra(BusinessActivity.bPhone, item.getPhoneNum());
        businessIntent.putExtra(BusinessActivity.bRating, (float)4.0);
        businessIntent.putExtra(BusinessActivity.bDist, (float)2.5);
        businessIntent.putExtra(BusinessActivity.bSnippet, item.getSnippet());
        businessIntent.putExtra(BusinessActivity.bImgSrc, item.getImgSrc());

        startActivity(businessIntent);

    }

}
