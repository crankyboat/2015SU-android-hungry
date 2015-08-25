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
    private int mLayoutResource;

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
        fragment.mLayoutResource = -100;
        return fragment;
    }

    public static PlaceholderFragment newInstance(CharSequence sectionName, int layoutResource) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_SECTION_NAME, sectionName);
        fragment.setArguments(args);
        fragment.mSectionName = sectionName;
        fragment.mLayoutResource = layoutResource;
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        if (mLayoutResource!=-100) {
            rootView = inflater.inflate(mLayoutResource, container, false);
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            mSectionNameView = (TextView) rootView.findViewById(R.id.section_label);
            mSectionNameView.setText("Hello "+mSectionName+"!");
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                mSectionNameView,
//                getArguments().getCharSequence(ARG_SECTION_NAME));
    }
}
