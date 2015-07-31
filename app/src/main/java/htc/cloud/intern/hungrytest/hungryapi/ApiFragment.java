package htc.cloud.intern.hungrytest.hungryapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.UserState;

/**
 * Created by intern on 7/28/15.
 */
public class ApiFragment extends Fragment implements AsyncResponse {

    private static final String ARG_SECTION_NAME = "section_name";
    private CharSequence mSectionName;
    private TextView mSectionNameView;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ApiFragment newInstance(CharSequence sectionName) {
        ApiFragment fragment = new ApiFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_SECTION_NAME, sectionName);
        fragment.setArguments(args);
        fragment.mSectionName = sectionName;
        return fragment;
    }

    public ApiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        mSectionNameView = (TextView) rootView.findViewById(R.id.section_label);
        mSectionNameView.setText("Hello " + mSectionName + "!");

        // Dummy user state
        UserState us = new UserState(getActivity().getBaseContext());
        us.setUserLocation(new LatLng(0, 0));
        us.setFeedback("business1", 1);
        us.setFeedback("business2", 100);

        HungryAsyncTask mHungryAsyncTask = new HungryAsyncTask();
        mHungryAsyncTask.setResponseDelegate(this);
        mHungryAsyncTask.execute(us);

        FeedbackAsyncTask mFeedbackAsyncTask = new FeedbackAsyncTask();
        mFeedbackAsyncTask.execute(us);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                mSectionNameView,
                getArguments().getCharSequence(ARG_SECTION_NAME));
    }

    @Override
    public void onPostExecute(JSONArray jsonArray) {
        try {
            mSectionNameView.setText(jsonArray.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
