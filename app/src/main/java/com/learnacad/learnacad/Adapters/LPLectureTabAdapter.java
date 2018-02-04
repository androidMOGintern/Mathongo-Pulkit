package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.LecturePlayerActivity;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.R;
import com.taishi.library.Indicator;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 11-07-2017.
 */

public class LPLectureTabAdapter extends RecyclerView.Adapter<LPLectureTabAdapter.LPLectureTabViewHolder> {

    Context mContext;
    ArrayList<Lecture> lectures;
    private int selectedPos = RecyclerView.NO_POSITION;

    public LPLectureTabAdapter(Context context,ArrayList<Lecture> lectures,int selectedPos){

        mContext = context;
        this.lectures = lectures;
        this.selectedPos = selectedPos;

    }

    @Override
    public LPLectureTabAdapter.LPLectureTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lecture_player_lecturetab_item_layout,parent,false);
        return new LPLectureTabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LPLectureTabAdapter.LPLectureTabViewHolder holder, final int position) {

        int num = position + 1;
        holder.titleTextView.setText(lectures.get(position).getName());
        holder.numTextView.setText(num + "");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPos = position;
                FlurryAgent.logEvent("Lesson_" + lectures.get(position).getName() + "_Clicked");

                notifyDataSetChanged();

                if(mContext instanceof LecturePlayerActivity){


                    ((LecturePlayerActivity) mContext).newDataLectureClicked(position);
                }

            }
        });

        holder.playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                selectedPos = position;
                FlurryAgent.logEvent("Lesson_" + lectures.get(position).getName() + "_Clicked");

                notifyDataSetChanged();

                if(mContext instanceof LecturePlayerActivity){


                        ((LecturePlayerActivity) mContext).newDataLectureClicked(position);
                }
            }
        });


        if(selectedPos == position){

            holder.indicator.setVisibility(View.VISIBLE);
            holder.playButton.setVisibility(View.GONE);
            holder.titleTextView.setTextColor(Color.parseColor("#1589ee"));
            holder.itemView.setClickable(false);
        }else{

            holder.indicator.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.VISIBLE);
            holder.titleTextView.setTextColor(Color.parseColor("#9e9e9e"));
            holder.itemView.setClickable(true);
        }

    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public class LPLectureTabViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView numTextView;
        ImageButton playButton;
        CardView completeCard;
        Indicator indicator;

        public LPLectureTabViewHolder(View itemView) {
            super(itemView);

            completeCard = (CardView) itemView.findViewById(R.id.lecturePlayerItemCardView);
            titleTextView = (TextView) itemView.findViewById(R.id.lecturePlayerLectureItemTitleTextView);
            numTextView = (TextView) itemView.findViewById(R.id.lecturePlayerLectureItemTextView);
            playButton = (ImageButton) itemView.findViewById(R.id.lecturePlayerLectureItemPlayButton);
            indicator = (Indicator) itemView.findViewById(R.id.indicator);
        }
    }
}
