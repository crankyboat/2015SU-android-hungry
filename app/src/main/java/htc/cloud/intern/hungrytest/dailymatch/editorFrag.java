package htc.cloud.intern.hungrytest.dailymatch;

/**
 * Created by cwhuang on 2015/8/20.
 */

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cwhuang on 2015/8/3.
 * Recommendation Fragment layout
 */

import htc.cloud.intern.hungrytest.R;

public class editorFrag extends Fragment{

    // Container Fragment must implement this interface
    public interface OnSelectListener {
        public void onObjectSelected(int res_index);
    }

    OnSelectListener mCallback;

    private Bundle obj = null;
    private int res_index;
    private String res_name,rating,distance,img;
    public float mPosX,mCurPosX,mPosY,mCurPosY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
            obj = getArguments();

        //get argument from parent fragment
            int layout = obj.getInt("layout");
            res_index = obj.getInt("res_index");
            res_index += 1;
            res_name = obj.getString("res_name");
            rating = obj.getString("rating");
            distance = obj.getString("distance");
            img = obj.getString("img");

            //Log.d("TAG", "editor oncreateview!!");

        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //pass argument from child to child
            onAttachFragment(getParentFragment());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_content();
        setListener();
        Log.d("TAG", "editorFrag onViewCreate!!");
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            mCallback = (OnSelectListener) fragment;
            Log.d("TAG","create mCallback!!");
        } catch (ClassCastException e)
        {
            throw new ClassCastException(fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    /*********** self_define method below *********/

    public void set_content(){
        TextView Name = (TextView) getView().findViewById(R.id.res_name);
        Name.setText(res_name);
        TextView Rating = (TextView)getView().findViewById(R.id.rating);
        Rating.setText(rating);
        TextView Distance = (TextView)getView().findViewById(R.id.distance);
        Distance.setText(distance);
        ImageView imageView = (ImageView) getView().findViewById(R.id.img);
        int imageResource = getResources().getIdentifier(img, null, getView().getContext().getPackageName());
        Drawable image = getResources().getDrawable(imageResource);
        imageView.setImageDrawable(image);
    }

    public void setListener() {
        getView().findViewById(R.id.icon_like).setOnClickListener(click_like);
        getView().findViewById(R.id.icon_cross).setOnClickListener(click_dislike);
        getView().findViewById(R.id.icon_info).setOnClickListener(click_info);
        getView().findViewById(R.id.restaurant).setOnTouchListener(_MyTouchListener);
        getView().findViewById(R.id.fragment).setOnDragListener(_MyDragListener);
    }

    private View.OnTouchListener _MyTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("TAG", "onTouch!!");
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                mPosX = motionEvent.getX();
                mPosY = motionEvent.getY();
                Log.d("TAG", "onTouch!!" + mPosX + "," + mPosY);
                return true;
            } else {
                Log.d("TAG", "NTouch!!");
                return false;
            }

        }
    };

    private View.OnDragListener _MyDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    //Log.d("TAG", "ACTION_DRAG_STARTED!!");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //Log.d("TAG", "ACTION_DRAG_ENTERED!!");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                        View _view = (View) event.getLocalState();
                        View _view1 = getView().findViewById(R.id.button);
                        ViewGroup _owner = (ViewGroup) _view.getParent();
                        _owner.removeView(_view);
                        _owner.removeView(_view1);
                        LinearLayout _container = (LinearLayout) v;
                        _container.addView(_view);
                        _container.addView(_view1);
                        _view.setVisibility(View.VISIBLE);
                        Log.d("TAG", "ACTION_DRAG_EXITED!!");
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    //Log.d("TAG", "ACTION_DRAG_LOCATION!!");
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    mCurPosX = event.getX();
                    mCurPosY = event.getY();

                    //slide left && slide right
                    if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > 600)) {
                        mCallback.onObjectSelected(res_index);
                    }else if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > 600)) {
                        mCallback.onObjectSelected(res_index);
                    }

                        View view = (View) event.getLocalState();
                        View view1 = getView().findViewById(R.id.button);
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        owner.removeView(view1);
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        container.addView(view1);
                        view.setVisibility(View.VISIBLE);

                        //Log.d("TAG","ACTION_DROP!!"+mCurPosX+","+mCurPosY);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.d("TAG", "ACTION_DRAG_ENDED!!");
                default:
                    //Log.d("TAG", "no work!!");
                    break;
            }
            return true;
        }
    };

    private View.OnClickListener click_like = new View.OnClickListener() {

        public void onClick(View view){
            mCallback.onObjectSelected(res_index);
            //Log.d("TAG", "up !!u like it!!!" + mCurPosY + "," + mPosY);

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View like = inflater.inflate(R.layout.like_show,
                    (ViewGroup) getView().findViewById(R.id.like_show_content));
            final Toast toast = new Toast(view.getContext());
            toast.setView(like);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);

        }
    };
    private View.OnClickListener click_dislike = new View.OnClickListener() {

        public void onClick(View view){
            mCallback.onObjectSelected(res_index);
            //Log.d("TAG", "up !!u dont like it!!!" + mCurPosY + "," + mPosY);

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View dislike = inflater.inflate(R.layout.dislike_show,
                    (ViewGroup) getView().findViewById(R.id.dislike_show_content));
            final Toast toast = new Toast(view.getContext());
            toast.setView(dislike);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);
        }
    };
    private View.OnClickListener click_info = new View.OnClickListener() {

        public void onClick(View view) {
            final Toast toast = Toast.makeText(view.getContext(),
                    "info",
                    Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);
        }
    };




}



