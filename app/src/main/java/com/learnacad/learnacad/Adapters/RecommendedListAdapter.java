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
 * Created by Sahil Malhotra on 07-06-2017.
 */

public class RecommendedListAdapter extends RecyclerView.Adapter<RecommendedListAdapter.RecommendedViewHolder>{

    Context mContext;
    ArrayList<String> titles;

    public RecommendedListAdapter(Context context,ArrayList<String>titleList){

        mContext = context;
        titles = titleList;
        Log.d("lolo","Reached adp");
    }
    @Override
    public RecommendedListAdapter.RecommendedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.recommended_feed_item_layout,parent,false);
        return new RecommendedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecommendedListAdapter.RecommendedViewHolder holder, final int position) {

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

    public class RecommendedViewHolder extends  RecyclerView.ViewHolder{

        TextView textView;

        public RecommendedViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }
}
