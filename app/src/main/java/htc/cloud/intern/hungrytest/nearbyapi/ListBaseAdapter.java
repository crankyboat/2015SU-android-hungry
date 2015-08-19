package htc.cloud.intern.hungrytest.nearbyapi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 7/28/15.
 */
public class ListBaseAdapter extends BaseAdapter {

    ArrayList<PlaceState> list = new ArrayList<PlaceState>();
    LayoutInflater inflater;
    Context context;

    public ListBaseAdapter(Context context, ArrayList<PlaceState> list) {
        this.list = new ArrayList<PlaceState>(list);
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setListData(ArrayList<PlaceState> newList) {
        list = newList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PlaceState getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, parent, false);
            mViewHolder = new ListViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ListViewHolder) convertView.getTag();
        }

        PlaceState currentPlaceState = getItem(position);

        mViewHolder.tv_title.setText(currentPlaceState.getName());
        mViewHolder.tv_desc.setText(currentPlaceState.getCategory());
        mViewHolder.tv_dist.setText(Math.round(currentPlaceState.getDist())+" km");
        mViewHolder.rb_rating.setRating((float)currentPlaceState.getRating());

        if ( !currentPlaceState.getImgSrc().equals("") ) {
            Ion.with(mViewHolder.iv_icon)
                    .placeholder(R.drawable.ic_stars_black_24dp)
                    .load(currentPlaceState.getImgSrc());
        }
        else {
            mViewHolder.iv_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mViewHolder.iv_icon.setImageResource(R.drawable.business_placeholder);
        }
        Log.i("ListBaseAdapter", "imgURL: "+currentPlaceState.getImgSrc());

        return convertView;
    }

    private class ListViewHolder {
        TextView tv_title, tv_desc, tv_dist;
        RatingBar rb_rating;
        ImageView iv_icon;

        public ListViewHolder(View item) {
            tv_title = (TextView) item.findViewById(R.id.tv_title);
            tv_desc = (TextView) item.findViewById(R.id.tv_desc);
            tv_dist= (TextView) item.findViewById(R.id.tv_dist);
            iv_icon = (ImageView) item.findViewById(R.id.iv_icon);
            rb_rating = (RatingBar) item.findViewById(R.id.rating_bar);
        }
    }

}
