package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.R;
import com.pchmn.materialchips.ChipView;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 18-08-2017.
 */

public class ChipsViewAdapeter extends RecyclerView.Adapter<ChipsViewAdapeter.ChipsViewHolder> {

    Context mContext;
    ArrayList<String> chipsTitles;
    CardView containingCardView;
    onChipDeleted listener;

    public  ChipsViewAdapeter (Context mContext,ArrayList<String> chipsTitles,CardView cardView,onChipDeleted listener){

        this.mContext = mContext;
        this.chipsTitles = chipsTitles;
        this.containingCardView = cardView;
        this.listener = listener;
        Log.d("uiui","Constructor called");
    }


    @Override
    public ChipsViewAdapeter.ChipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.chipitem_layout,parent,false);
        Log.d("uiui","onCreateViewHolder called");
        return new ChipsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChipsViewAdapeter.ChipsViewHolder holder, final int position) {

        holder.chipView.setLabel(chipsTitles.get(position));

        holder.chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipsTitles.remove(position);
           //     notifyItemChanged(position);
                notifyDataSetChanged();
                listener.justCallingFunction(chipsTitles);

                if(chipsTitles.isEmpty()){

                    containingCardView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d("uiui", String.valueOf(chipsTitles.size()));
        return chipsTitles.size();


    }


    public class ChipsViewHolder extends RecyclerView.ViewHolder  {


        ChipView chipView;

        public ChipsViewHolder(View itemView) {
            super(itemView);

            chipView = itemView.findViewById(R.id.chipItemLayout);
        }
    }

    public interface onChipDeleted{

        void justCallingFunction(ArrayList<String> chipTitles);
    }
}
