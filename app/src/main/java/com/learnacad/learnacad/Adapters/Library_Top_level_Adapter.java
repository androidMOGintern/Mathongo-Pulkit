package com.learnacad.learnacad.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.learnacad.learnacad.Fragments.LibraryCourseListFragment;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 18-06-2017.
 */

public class Library_Top_level_Adapter extends RecyclerView.Adapter<Library_Top_level_Adapter.LibraryTopViewHolder> {

    Context mContext;
    ArrayList<String> titles;

    public Library_Top_level_Adapter(Context context, ArrayList<String> titles){

        mContext = context;
        this.titles = titles;

    }

    @Override
    public Library_Top_level_Adapter.LibraryTopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.library_top_item_layout,parent,false);
        return new LibraryTopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Library_Top_level_Adapter.LibraryTopViewHolder holder, final int position) {

        holder.button.setText("LOL " + position);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(mContext, "Clicked the Button " + position, Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                LibraryCourseListFragment libraryCourseListFragment = new LibraryCourseListFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,libraryCourseListFragment).addToBackStack(null).commit();
                activity.setTitle("LOL" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class LibraryTopViewHolder extends RecyclerView.ViewHolder{

        Button button;

        public LibraryTopViewHolder(View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.libraryTopLevelButton);
        }
    }
}
