package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.learnacad.learnacad.Activities.LecturePlayerActivity;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 10-06-2017.
 */

public class BookmarksListAdapter  extends RecyclerView.Adapter<BookmarksListAdapter .BookmarksListViewHolder>{

    Context mContext;
    ArrayList<Lecture> bookmarks;

    public BookmarksListAdapter (Context context,ArrayList<Lecture>titleList){

        mContext = context;
        bookmarks = titleList;
        Log.d("lolo","Reached adp");
    }
    @Override
    public BookmarksListAdapter .BookmarksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.lcclectures_item_layout,parent,false);
        return new BookmarksListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookmarksListAdapter.BookmarksListViewHolder holder, final int position) {

        holder.textViewNumber.setText(position + 1 + ". ");
        holder.durationTextView.setText(bookmarks.get(position).getDuration());
        holder.titleTextView.setText(bookmarks.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LecturePlayerActivity.class);
                intent.putExtra("selectedLecture", bookmarks.get(position));
                intent.putExtra("selectedPosition", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("lectureList", bookmarks);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.d("lolo", String.valueOf(bookmarks.size()));
        return bookmarks.size();
    }

    public class BookmarksListViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewNumber;
        TextView titleTextView;
        TextView durationTextView;
        ImageButton playButton;
        public BookmarksListViewHolder(View itemView) {
            super(itemView);

            textViewNumber = itemView.findViewById(R.id.lcclectures_item_number);
            titleTextView = itemView.findViewById(R.id.lcclectures_item_titleTextView);
            durationTextView = itemView.findViewById(R.id.lcclectures_item_DurartionTextView);
            playButton = itemView.findViewById(R.id.lcclectures_item_playButton);
        }
    }
}
