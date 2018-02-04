package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.LibraryCourseContentActivity;
import com.learnacad.learnacad.Models.Minicourse;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sahil Malhotra on 19-06-2017.
 */

public class LibraryCourseListAdapter extends RecyclerView.Adapter<LibraryCourseListAdapter.LibraryCourseViewHolder> implements
        Filterable{

    Context mContext;
    ArrayList<Minicourse> minicourses;
    SwipeRefreshLayout relativeLayout;
    ArrayList<Tutor> tutors;
    ArrayList<Minicourse> minicoursesFiltered;


    public  LibraryCourseListAdapter(Context context, ArrayList<Minicourse> minicoursesList, SwipeRefreshLayout relativeLayout, ArrayList<Tutor> tutors){

        mContext = context;
        this.tutors = tutors;
        this.minicourses = minicoursesList;
        this.relativeLayout = relativeLayout;
        this.minicoursesFiltered = minicoursesList;

    }

    @Override
    public LibraryCourseListAdapter.LibraryCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.library_course_list_item_layout,parent,false);
        return new LibraryCourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LibraryCourseListAdapter.LibraryCourseViewHolder holder, final int position) {


        final Minicourse minicourse = minicoursesFiltered.get(position);

        holder.textViewCourseItemTutorName.setText(minicourse.getTutorName());
        holder.textViewCourseItemDescription.setText(minicourse.getDescription());
        holder.textViewCourseItemTitle.setText(minicourse.getName());
        holder.ratingBar.setRating(minicourse.getRating());
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#ffb75d"), PorterDuff.Mode.SRC_ATOP);

        String category = minicourse.getRelevance();

        Log.d("care",category);

        if(category.contains("Mains") && category.contains("CBSE")){

            holder.linearColorView.setBackgroundResource(R.drawable.jeecbse);
        }
        else if(category.contains("Mains") && category.contains("Advanced")){

            holder.linearColorView.setBackgroundResource(R.drawable.mainadvanced);
        }
        else if(category.contentEquals("CBSE")){

            holder.linearColorView.setBackgroundResource(R.drawable.onlycbsehdpi);
        }
        else if(category.contentEquals("JEE Mains")){

            holder.linearColorView.setBackgroundResource(R.drawable.onlymainshdpi);
        }
        else if(category.contentEquals("JEE Advance")){

            holder.linearColorView.setBackgroundResource(R.drawable.onlyadvancehdpi);
        }else{

            holder.linearColorView.setBackgroundResource(R.drawable.categoryall);
        }


        if(tutors.size() > 0) {

            Tutor t = tutors.get(position);

            if (t.getImgUrl() == null || t.getImgUrl().length() == 0 || t.getImgUrl().isEmpty()) {

                holder.circleImageView.setImageResource(R.drawable.teachersicon);
            } else {

//
//            Picasso.Builder builder = new Picasso.Builder(mContext);
//            builder.listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    Log.d("picassonError",exception.getLocalizedMessage());
//                    Log.d("picassonError",exception.getMessage());
//                }
//            });

                StringBuilder builder1 = new StringBuilder();
                builder1.append(Api_Urls.BASE_URL);
                builder1.append("images/")
                        .append(minicourse.getTutorImageUrl())
                        .append(".jpg");

                Picasso.with(mContext).load(builder1.toString()).error(R.drawable.teachersicon).into(holder.circleImageView);
            }
        }
/*
        if(minicourse.getEnrolled()){

            holder.enrollButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
            holder.enrollButton.setText("Enrolled");
        }
*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlurryAgent.logEvent("Course_" + minicourse.getName() + "_Clicked");

                Intent intent = new Intent(mContext,LibraryCourseContentActivity.class);
                String pid = String.valueOf(minicourse.getCourse_id()) + String.valueOf(tutors.get(position).getTutor_id());
                intent.putExtra("MINICOURSE_ID",minicourse.getCourse_id());
                intent.putExtra("PROCESS_ID",pid);
                intent.putExtra("ENROLLED",minicourse.getEnrolled());
                mContext.startActivity(intent);

                }
             });

/*
        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.bookmarkButton.setImageResource(R.drawable.bookmarkactivebluefilled);
                Snackbar.make(relativeLayout,"Added to your bookmarks",Snackbar.LENGTH_SHORT).show();
            }
        });*/

/*        holder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(minicourse.getEnrolled()){

                    Snackbar.make(relativeLayout,"Already Enrolled",Snackbar.LENGTH_SHORT).show();
                }else{

                    holder.enrollButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
                    holder.enrollButton.setText("Enrolled");

                    enrollClickListener.EnrollButtonClicked(minicourse);

                        new SweetAlertDialog(mContext,SweetAlertDialog.SUCCESS_TYPE)
                                .setContentText("You have successfully enrolled")
                                .setTitleText("Enrolled !")
                                .show();


                }
            }
        });*/

        }


    @Override
    public int getItemCount() {
        Log.d("toing",minicoursesFiltered.size() + "");
        return minicoursesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                Log.d("searchCheck",charString);
                Log.d("searchCheck",charString.toLowerCase());
                Log.d("searchCheck",charString.toUpperCase());


                if(charString.isEmpty()){

                    minicoursesFiltered = minicourses;
                }else{

                    ArrayList<Minicourse> filteredMinicourses = new ArrayList<>();

                    for(Minicourse minicourse : minicourses){

                        if( (minicourse.getName().contains(charString)) || (minicourse.getTutorName().contains(charString)) || (minicourse.getDescription().contains(charString))
                                || (minicourse.getName().toLowerCase().contains(charString.toLowerCase()))  || (minicourse.getDescription().toLowerCase().contains(charString.toLowerCase()))
                                || (minicourse.getTutorName().toLowerCase().contains(charString.toLowerCase()))  || (minicourse.getName().toUpperCase().contains(charString.toUpperCase()))  || (minicourse.getDescription().toUpperCase().contains(charString.toUpperCase()))
                                || (minicourse.getTutorName().toUpperCase().contains(charString.toUpperCase())) ){

                            Log.d("searchCheck",minicourse.getName());

                            filteredMinicourses.add(minicourse);
                        }
                    }

                    minicoursesFiltered = filteredMinicourses;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = minicoursesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                minicoursesFiltered = (ArrayList<Minicourse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class LibraryCourseViewHolder extends RecyclerView.ViewHolder{

        TextView textViewCourseItemTitle;
        TextView textViewCourseItemDescription;
        TextView textViewCourseItemTutorName;
//        Button enrollButton;
        RatingBar ratingBar;
        View linearColorView;
        CircleImageView circleImageView;

        public LibraryCourseViewHolder(View itemView) {
            super(itemView);

//            enrollButton = (Button) itemView.findViewById(R.id.LibraryCourseItemEnrollButton);
            linearColorView = itemView.findViewById(R.id.categoryLineColor);
            ratingBar = (RatingBar) itemView.findViewById(R.id.LibraryCourseItemRatingbar);
//            bookmarkButton = (ImageButton) itemView.findViewById(R.id.LibraryCourseItemBookMarkButton);
            textViewCourseItemTitle = (TextView) itemView.findViewById(R.id.LibraryCourseItemTitle);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.LibraryCourseItemCircleImageView);
            textViewCourseItemDescription = (TextView) itemView.findViewById(R.id.LibraryCourseItemDescription);
            textViewCourseItemTutorName = (TextView) itemView.findViewById(R.id.LibraryCourseItemTeacherNameTextView);
        }
    }
/*
    public interface EnrollbuttonClicklistener{

        void EnrollButtonClicked(Minicourse m);
    }*/
}
