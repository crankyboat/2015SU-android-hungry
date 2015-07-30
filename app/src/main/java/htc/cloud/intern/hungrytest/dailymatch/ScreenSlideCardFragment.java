package htc.cloud.intern.hungrytest.dailymatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/27/15.
 */
public class ScreenSlideCardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_view_card, container, false);

        return rootView;
    }

}