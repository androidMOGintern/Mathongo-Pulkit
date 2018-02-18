package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 10-06-2017.
 */

public class EnrolledListAdapter extends RecyclerView.Adapter<EnrolledListAdapter.EnrolledViewHolder> {

    Context mContext;
    ArrayList<String>titles;

    public EnrolledListAdapter(Context context, ArrayList<String> titles){

        mContext = context;
        this.titles = titles;

    }

    @Override
    public EnrolledListAdapter.EnrolledViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.enrolled_mycourses_item_fragment,parent,false);
        return new EnrolledViewHolder(v);
    }


    @Override
    public void onBindViewHolder(EnrolledListAdapter.EnrolledViewHolder holder, int position) {

        holder.textView.setText("LOL" + " " +position);

    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class EnrolledViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public  EnrolledViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }

    }
}
