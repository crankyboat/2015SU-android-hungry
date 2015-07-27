package htc.cloud.intern.hungrytest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by intern on 7/24/15.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    private CharSequence mSectionName;
    private TextView mSectionNameView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(CharSequence sectionName) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_SECTION_NAME, sectionName);
        fragment.setArguments(args);
        fragment.mSectionName = sectionName;
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        mSectionNameView = (TextView) rootView.findViewById(R.id.section_label);
        mSectionNameView.setText("Hello "+mSectionName+"!");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                mSectionNameView,
                getArguments().getCharSequence(ARG_SECTION_NAME));
    }
}
