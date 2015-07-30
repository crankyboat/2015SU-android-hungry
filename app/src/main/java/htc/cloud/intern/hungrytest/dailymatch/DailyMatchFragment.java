package htc.cloud.intern.hungrytest.dailymatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/27/15.
 */
public class DailyMatchFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    private CharSequence mSectionName;
    private TextView mSectionNameView;

    public static final int NUM_PAGES = 10;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DailyMatchFragment newInstance(CharSequence sectionName) {
        DailyMatchFragment fragment = new DailyMatchFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_SECTION_NAME, sectionName);
        fragment.setArguments(args);
        fragment.mSectionName = sectionName;
        return fragment;
    }

    public DailyMatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dailymatch, container, false);

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(MainActivity.mFragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                mSectionNameView,
                getArguments().getCharSequence(ARG_SECTION_NAME));
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlideCardFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
