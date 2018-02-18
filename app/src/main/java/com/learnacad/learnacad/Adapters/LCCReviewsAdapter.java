package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.learnacad.learnacad.Models.Reviews;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/* Created by Sahil Malhotra on 27-08-2017. */



public class LCCReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    RecyclerView recyclerView;
    private boolean isLoading = true;
    ArrayList<Reviews> reviewsArrayList;
    private static final int TYPE_ITEM = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem,totalItemCount;
    private static final int TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;



    public LCCReviewsAdapter(Context context, ArrayList<Reviews> reviewsArrayList,RecyclerView recyclerView){

        mContext = context;
        this.reviewsArrayList = reviewsArrayList;
        this.recyclerView = recyclerView;

//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//
//                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
//
//                    if(onLoadMoreListener != null){
//
//                        onLoadMoreListener.onLoadMore();
//                    }
//                    Log.d("0p0p","isLoading set to true");
//
//                    isLoading = true;
//                }
//            }
//        });

    }

//    @Override
//    public int getItemViewType(int position) {
//        return reviewsArrayList.get(position) == null ? TYPE_LOADING : TYPE_ITEM;
//    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){

        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



           View view = LayoutInflater.from(mContext).inflate(R.layout.reviews_item_layout,parent,false);
           return new UserViewHolder(view);


//       else{
//
//            View view = LayoutInflater.from(mContext).inflate(R.layout.footer,parent,false);
//           return new LoadMoreHolder(view);
//      }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof UserViewHolder) {

            UserViewHolder uHolder = (UserViewHolder) holder;
            uHolder.nameTextView.setText(reviewsArrayList.get(position).getStudentName());
            String desc = reviewsArrayList.get(position).getDescription();


            if (desc.isEmpty()) {

                uHolder.descTextView.setVisibility(View.GONE);
            } else {

                uHolder.descTextView.setText(desc);
            }

            uHolder.ratingBar.setRating(reviewsArrayList.get(position).getRating());

//        }else if(holder instanceof LoadMoreHolder){
//
//                LoadMoreHolder lholder = (LoadMoreHolder) holder;
//                lholder.progressBar.setIndeterminate(true);
//        }

        }

    }

    @Override
    public int getItemCount() {
        Log.d("0p0p","getItem count = " + reviewsArrayList.size());
        return reviewsArrayList == null ? 0 : reviewsArrayList.size();
    }

    public void setLoaded(){

        isLoading = false;
    }

//    @Override
//    public int getItemViewType(int position) {
//
//            if(position == reviewsArrayList.size() + 1){
//
//                return TYPE_LOADING;
//            }else{
//
//                return TYPE_ITEM;
//            }
//    }

    public  class UserViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        RatingBar ratingBar;
        TextView descTextView;

        public UserViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.reviews_item_nameTextView);
            ratingBar = itemView.findViewById(R.id.reviews_item_ratingBar);
            descTextView = itemView.findViewById(R.id.reviews_item_descriptionTextView);
        }


    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder{

        public ProgressBar progressBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface OnLoadMoreListener{

        void onLoadMore();
    }
}
