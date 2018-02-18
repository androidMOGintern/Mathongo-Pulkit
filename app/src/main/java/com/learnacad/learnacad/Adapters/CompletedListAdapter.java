package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 09-06-2017.
 */

public class CompletedListAdapter extends RecyclerView.Adapter<CompletedListAdapter.CompletedListViewHolder>{

    Context mContext;
    ArrayList<String> titles;

    public CompletedListAdapter(Context context,ArrayList<String>titleList){

        mContext = context;
        titles = titleList;
        Log.d("lolo","Reached adp Completed");
    }
    @Override
    public CompletedListAdapter.CompletedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.offlineline_feed_fragment_item_layout,parent,false);
        return new CompletedListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CompletedListAdapter.CompletedListViewHolder holder, final int position) {

        holder.textView.setText("Lol" + " " + position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "LOL" + " " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        Log.d("lolo", String.valueOf(titles.size()));
        return titles.size();
    }

    public class CompletedListViewHolder extends  RecyclerView.ViewHolder{

        TextView textView;

        public CompletedListViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }
}

