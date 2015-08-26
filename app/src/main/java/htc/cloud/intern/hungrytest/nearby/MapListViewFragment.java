package htc.cloud.intern.hungrytest.nearby;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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
        mListView.setDivider(null);
        mListView.setEmptyView(rootView.findViewById(android.R.id.empty));
        ((ProgressBar)mListView.getEmptyView()).getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.my_primary), PorterDuff.Mode.MULTIPLY);
        setListAdapter(new ListBaseAdapter(getActivity(), mList));

        return rootView;
    }

    public void onLocationChanged(ArrayList<PlaceState> likelyPlaces){

        mList = new ArrayList<PlaceState>(likelyPlaces);
        ((ListBaseAdapter)getListAdapter()).setListData(mList);
        ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
        mListView.setEnabled(false);
        mListView.requestLayout();
        mListView.setEnabled(true);

    }

    @Override
    public void onListItemClick (ListView listView, View view, int position, long id) {

        PlaceState business = (PlaceState) getListView().getItemAtPosition(position);
        Intent businessIntent = BusinessActivity.setUpBusinessIntent(getActivity(), business);
        startActivity(businessIntent);

    }

}
