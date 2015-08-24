package htc.cloud.intern.hungrytest.dailymatch;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.PlaceholderFragment;
import htc.cloud.intern.hungrytest.R;
import htc.cloud.intern.hungrytest.UserState;
import htc.cloud.intern.hungrytest.hungryapi.FeedbackAsyncTask;

/**
 * Created by intern on 7/27/15.
 */
public class DailyMatchFragment extends Fragment implements editorFrag.OnSelectListener{


    private static final String ARG_SECTION_NAME = "section_name";
    private CharSequence mSectionName;
    private TextView mSectionNameView;
    private MainActivity mActivity;

    private ArrayList<PlaceState> restaurant_content;
    private Bundle args = null;
    private UserState mUserState;

    //constructor
    public DailyMatchFragment() {}

    public static Set<Integer> randIntSetWithImg(ArrayList<PlaceState> cachedPlaces, int maxPlaces, Random rand) {
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < maxPlaces)
        {
            Integer next = rand.nextInt(cachedPlaces.size());
            if (!cachedPlaces.get(next).getImgSrc().equals("")) {
                generated.add(next);
            }
        }
        return generated;
    }

    public void setPositiveFeedback(int businessIndex) {
        mUserState.setFeedback(restaurant_content.get(businessIndex).getId(), 1);
    }

    public void setNegativeFeedback(int businessIndex) {
        mUserState.setFeedback(restaurant_content.get(businessIndex).getId(), -1);
    }

    private Bundle set_layout_content(Integer layout, Integer res_index,
                                      String res_name, double rating, double distance, String img){
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        args.putInt("res_index", res_index);
        args.putString("res_name", res_name);
        args.putDouble("rating", rating);
        args.putDouble("distance", distance);
        args.putString("img", img);
        return args;
    }

    //onObjectSelected
    public void onObjectSelected(int res_index) {

        Log.d("TAG", "!!!" + res_index);

        // END OF LIST
        if (res_index >= restaurant_content.size()) {

            FeedbackAsyncTask mFeedbackAsyncTask = new FeedbackAsyncTask();
            mFeedbackAsyncTask.execute(mUserState);

            getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_out, R.anim.fade_in)
                    .replace(R.id.fragment_daily, PlaceholderFragment.newInstance("", R.layout.fragment_dailymatch_end))
                    .commit();

            return;
        }

        args = set_layout_content(R.layout.fragment_restaurant,
                res_index,
                restaurant_content.get(res_index).getName(),
                restaurant_content.get(res_index).getRating(),
                restaurant_content.get(res_index).getDist(),
                restaurant_content.get(res_index).getImgSrc());

        editorFrag new_frag = new editorFrag();
        new_frag.setArguments(args);

        //method1 : replace fragment with animation
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_out, R.anim.fade_in)
                .replace(R.id.fragment_daily, new_frag)
                .commit();

    }


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

    //Set a restaurant object content
//    private PlaceState set_content(String res_name,String rating,String distance, String img){
//        return new PlaceState(res_name,rating,distance,img);
//    }

    //Add a restaurant object to arraylist
    private void add_content(){

        ArrayList<PlaceState> cachedPlaces = mActivity.mNearbyApiFragment.getCachedPlaces();
        if (cachedPlaces==null) {
            Toast.makeText(mActivity, "Network error.", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            int MAX_FEEDBACK = 10;
            Set<Integer> randSetWithImg = randIntSetWithImg(cachedPlaces, MAX_FEEDBACK, new Random());
            for (Integer r : randSetWithImg) {
                restaurant_content.add(cachedPlaces.get(r));
            }
            return;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        mUserState = mActivity.mUserState;

        add_content();
        Log.d("TAG", "F : onCreate!!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dailymatch, container, false);

        if(rootView.findViewById(R.id.fragment_daily) != null
                && restaurant_content!=null && restaurant_content.size()>0) {

            // Create an instance of editorFrag
            editorFrag frag = new editorFrag();

            //Set argument of restaurant && 0 is the first restaurant
            args = set_layout_content(R.layout.fragment_restaurant,
                                      0,
                                      restaurant_content.get(0).getName(),
                                      restaurant_content.get(0).getRating(),
                                      restaurant_content.get(0).getDist(),
                                      restaurant_content.get(0).getImgSrc());

            frag.setArguments(args);


            //add fragment to the fragment container layout
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_daily, frag)
                .addToBackStack(null)
                .commit();

        }
        else {
            // TODO
            // If restaurant_content is empty?
        }

        Log.d("TAG", "onCreateView!!");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("TAG", "F : onViewCreate!!");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                mSectionNameView,
                getArguments().getCharSequence(ARG_SECTION_NAME));

        restaurant_content = new ArrayList<PlaceState>();

        Log.d("TAG", "F : onAttach!!");
    }

}
