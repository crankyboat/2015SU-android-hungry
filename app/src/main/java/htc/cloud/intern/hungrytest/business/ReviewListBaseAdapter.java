package htc.cloud.intern.hungrytest.business;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.R;

/**
 * Created by intern on 8/16/15.
 */
public class ReviewListBaseAdapter extends BaseAdapter {

    private ArrayList<ReviewItem> list;
    LayoutInflater inflater;
    Context context;

    public ReviewListBaseAdapter(Context context, ArrayList<ReviewItem> list) {
        this.list = new ArrayList<ReviewItem>(list);
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ReviewItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_reviewlist, parent, false);
            mViewHolder = new ListViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ListViewHolder) convertView.getTag();
        }

        ReviewItem currentReview = getItem(position);
        mViewHolder.tv_username.setText(currentReview.getUserName());
        mViewHolder.tv_date.setText(currentReview.getDate());
        mViewHolder.tv_content.setText(currentReview.getContent());
        mViewHolder.rb_rating.setRating(currentReview.getRating());

        if ( !currentReview.getUserImgUrl().equals("") ) {
            Ion.with(mViewHolder.iv_usericon)
                    .placeholder(R.drawable.ic_stars_black_24dp)
                    .load(currentReview.getUserImgUrl());
        }
        else {
            mViewHolder.iv_usericon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mViewHolder.iv_usericon.setImageResource(R.drawable.business_image);
        }

        return convertView;

    }

    private class ListViewHolder {
        TextView tv_username, tv_date, tv_content;
        RatingBar rb_rating;
        ImageView iv_usericon;

        public ListViewHolder(View item) {

            tv_username = (TextView) item.findViewById(R.id.user_name);
            tv_date = (TextView) item.findViewById(R.id.review_date);
            tv_content = (TextView) item.findViewById(R.id.review_content);
            rb_rating = (RatingBar) item.findViewById(R.id.review_rating);
            iv_usericon = (ImageView) item.findViewById(R.id.user_icon);
        }
    }

}
