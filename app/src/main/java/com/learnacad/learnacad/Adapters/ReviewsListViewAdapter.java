package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.learnacad.learnacad.Models.Reviews;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 29-09-2017.
 */

public class ReviewsListViewAdapter extends ArrayAdapter<Reviews> {
    private Context mContext;
    ArrayList<Reviews> reviewsArrayList;

    public ReviewsListViewAdapter(Context mContext, ArrayList<Reviews> reviewsArrayList){
        super(mContext,0,reviewsArrayList);
        this.mContext = mContext;
        this.reviewsArrayList = reviewsArrayList;
        Log.d("0p0p",reviewsArrayList.size() + "");

    }

    static class ReviewsViewHolder{

        TextView nameTextView;
        RatingBar ratingBar;
        TextView descTextView;

            ReviewsViewHolder(TextView nameTextView,TextView descTextView, RatingBar ratingBar){

                    this.descTextView = descTextView;
                    this.nameTextView = nameTextView;
                    this.ratingBar = ratingBar;
            }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){

            convertView = View.inflate(mContext,R.layout.reviews_item_layout,null);


            TextView nameTextView = convertView.findViewById(R.id.reviews_item_nameTextView);
            RatingBar ratingBar = convertView.findViewById(R.id.reviews_item_ratingBar);
            TextView descTextView = convertView.findViewById(R.id.reviews_item_descriptionTextView);
            ReviewsViewHolder reviewsViewHolder = new ReviewsViewHolder(nameTextView,descTextView,ratingBar);
            convertView.setTag(reviewsViewHolder);

        }

        ReviewsViewHolder reviewsViewHolder = (ReviewsViewHolder) convertView.getTag();
        Reviews reviews = reviewsArrayList.get(position);
        reviewsViewHolder.nameTextView.setText(reviews.getStudentName());
        int length = reviews.getDescription().length();

        SpannableString text = new SpannableString("....Read More");



        if(length > 60 && (Math.abs(length - 60) > 10) ){

          //  StringBuilder builder = new StringBuilder();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(reviews.getDescription(),0,45);
            spannableStringBuilder.append(text);

           // reviewsViewHolder.descTextView.setText(Html.fromHtml(builder.toString() + " " + moreSpannable,Html.FROM_HTML_MODE_LEGACY));

            reviewsViewHolder.descTextView.setText(spannableStringBuilder.toString());
        }else{

            reviewsViewHolder.descTextView.setText(reviews.getDescription());
        }


        reviewsViewHolder.ratingBar.setRating(reviews.getRating());
        Drawable drawable = reviewsViewHolder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#ffb75d"), PorterDuff.Mode.SRC_ATOP);

        Log.d("0p0p",reviews.getStudentName() + " getView");

        return convertView;
    }

}
