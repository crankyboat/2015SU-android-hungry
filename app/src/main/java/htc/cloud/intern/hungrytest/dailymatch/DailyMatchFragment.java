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

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.MainActivity;
import htc.cloud.intern.hungrytest.PlaceholderFragment;
import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/27/15.
 */
public class DailyMatchFragment extends Fragment implements editorFrag.OnSelectListener{


    private static final String ARG_SECTION_NAME = "section_name";
    private CharSequence mSectionName;
    private TextView mSectionNameView;

    private ArrayList<object_content> restaurant_content = new ArrayList<object_content>();
    private Bundle args = null;

    //constructor
    public DailyMatchFragment() {}

    private Bundle set_layout_content(Integer layout,Integer res_index,String res_name,String rating,String distance, String img){
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        args.putInt("res_index", res_index);
        args.putString("res_name", res_name);
        args.putString("rating", rating);
        args.putString("distance", distance);
        args.putString("img", img);
        return args;
    }

    //onObjectSelected
    public void onObjectSelected(int res_index) {

          Log.d("TAG", "res_index:" + res_index+"!!!");

          args = set_layout_content(R.layout.fragment_restaurant,
                  res_index,
                  restaurant_content.get(res_index).res_name,
                  restaurant_content.get(res_index).rating,
                  restaurant_content.get(res_index).distance,
                  restaurant_content.get(res_index).img);

          editorFrag new_frag = new editorFrag();
          new_frag.setArguments(args);

          //method1 : replace fragment with animation
          getChildFragmentManager()
                  .beginTransaction()
                  .setCustomAnimations(R.anim.fade_out, R.anim.fade_in)
                  .replace(R.id.fragment_daily, new_frag)
                  .addToBackStack(null)
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
    private object_content set_content(String res_name,String rating,String distance, String img){
        return new object_content(res_name,rating,distance,img);
    }
    //Add a restaurant object to arraylist
    private void add_content(){

        //two fake data
        restaurant_content.add(set_content("橋北屋","3.8","17.3","@drawable/img_pizza"));
        restaurant_content.add(set_content("鼎泰豐","4.5","14.3","@drawable/img_pizza"));
        //restaurant_content.add(set_content("鼎王","4.0","10.0","@drawable/image_6"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        add_content();
        Log.d("TAG", "F : onCreate!!");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dailymatch, container, false);
        if(rootView.findViewById(R.id.fragment_daily) != null) {

            // Create an instance of editorFrag
                editorFrag frag = new editorFrag();

            //Set argument of restaurant && 0 is the first restaurant
                args = set_layout_content(R.layout.fragment_restaurant,
                                          0,
                                          restaurant_content.get(0).res_name,
                                          restaurant_content.get(0).rating,
                                          restaurant_content.get(0).distance,
                                          restaurant_content.get(0).img);
                frag.setArguments(args);


            //add fragment to the fragment container layout
                getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_daily, frag)
                    .addToBackStack(null)
                    .commit();

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
        Log.d("TAG", "F : onAttach!!");
    }

}
