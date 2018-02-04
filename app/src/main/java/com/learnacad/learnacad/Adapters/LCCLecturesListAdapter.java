package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.LecturePlayerActivity;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Sahil Malhotra on 30-06-2017.
 */

public class LCCLecturesListAdapter extends RecyclerView.Adapter<LCCLecturesListAdapter.LCCLecturesListViewHolder> {

    Context mContext;
    ArrayList<Lecture> lessons;
    Tutor tutor;
    public boolean enrolled;
    CoordinatorLayout coordinatorLayout;

    public LCCLecturesListAdapter(Context context, ArrayList<Lecture> lessons,Tutor tutor,boolean enrolled,CoordinatorLayout coordinatorLayout){

        mContext = context;
        this.lessons = lessons;
        this.tutor = tutor;
        this.coordinatorLayout = coordinatorLayout;
        this.enrolled = enrolled;

        Log.d("789456123","LCCLecturesListAdapter constructor called with enrolled = " + enrolled);
    }

    @Override
    public LCCLecturesListAdapter.LCCLecturesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lcclectures_item_layout,parent,false);
        return new LCCLecturesListViewHolder(view) ;
    }

    public void isEnrolledChanged(){

        this.enrolled = true;
        Log.d("789456123","is EnrollChanged adapter = " + enrolled);
    }

    @Override
    public void onBindViewHolder(LCCLecturesListAdapter.LCCLecturesListViewHolder holder, final int position) {

        int num = position + 1;
        holder.numberTextview.setText(num + ". ");
        holder.titleTextView.setText(lessons.get(position).getName());
        holder.durationTextView.setText(lessons.get(position).getDuration());
//        holder.updatedTextView.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlurryAgent.logEvent("Lesson_" + lessons.get(position).getName() + "_Clicked");

                if(!enrolled){

                        new SweetAlertDialog(mContext,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Please Enroll to view the lectures")
                                .setTitleText("Oops...")
                                .show();
                }else {



//                mContext.startActivity(new Intent(mContext, LecturePlayerActivity.class));
                    Intent intent = new Intent(mContext, LecturePlayerActivity.class);
                    intent.putExtra("selectedLecture", lessons.get(position));
                    intent.putExtra("selectedPosition", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("lectureList", lessons);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlurryAgent.logEvent("Lesson_" + lessons.get(position).getName() + "_Clicked");


                if(!enrolled){

                    new SweetAlertDialog(mContext,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Please Enroll to view the lectures")
                            .setTitleText("Oops...")
                            .show();
                }else {

//                mContext.startActivity(new Intent(mContext, LecturePlayerActivity.class));
                    Intent intent = new Intent(mContext, LecturePlayerActivity.class);
                    intent.putExtra("selectedLecture", lessons.get(position));
                    intent.putExtra("selectedPosition", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("lectureList", lessons);
                    mContext.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class LCCLecturesListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView durationTextView;
        TextView numberTextview;
        ImageButton playButton;

        public LCCLecturesListViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.lcclectures_item_titleTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.lcclectures_item_DurartionTextView);
            numberTextview = (TextView) itemView.findViewById(R.id.lcclectures_item_number);
            playButton = (ImageButton) itemView.findViewById(R.id.lcclectures_item_playButton);

        }
    }
}
