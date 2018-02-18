package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learnacad.learnacad.Models.Notifications;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 16-10-2017.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder> {

    private Context mContext;
    private ArrayList<Notifications> notificationsArrayList;

    public NotificationsListAdapter(Context mContext, ArrayList<Notifications> notificationsArrayList) {
        this.mContext = mContext;
        this.notificationsArrayList = notificationsArrayList;
    }

    @Override
    public NotificationsListAdapter.NotificationsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notifications_item_layout,parent,false);
        return new NotificationsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsListAdapter.NotificationsListViewHolder holder, int position) {

        holder.titleTextView.setText(notificationsArrayList.get(position).getTitle());
        holder.descTextView.setText(notificationsArrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class NotificationsListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView descTextView;
        public RelativeLayout viewForeground,viewBackGround;

        public NotificationsListViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.notificationsItemTitleTextView);
            descTextView = itemView.findViewById(R.id.notificationsItemDescriptionTextView);
            viewBackGround = itemView.findViewById(R.id.relativeLayout_background);
            viewForeground = itemView.findViewById(R.id.relativeLayout_Foreground);
        }
    }

    public void removeItem(int position){

        notificationsArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Notifications notifications,int position){

        notificationsArrayList.add(position,notifications);
        notifyItemInserted(position);
    }
}
