package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.CompletedListAdapter;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 09-06-2017.
 */

public class CompletedMyCoursesFragment extends Fragment {

    RecyclerView recyclerView;
    CompletedListAdapter completedListAdapter;
    ArrayList<String> titles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.completed_mycourses_fragment_layout,container,false);
        recyclerView = v.findViewById(R.id.completedRecyclerViewList);
        titles = new ArrayList<>();
        completedListAdapter = new CompletedListAdapter(getActivity(),titles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(completedListAdapter);
        fetchTitles();
        return v;
    }

    private void fetchTitles() {



        for(int i = 0; i < 5; ++i){

            titles.add("Title" + " " + i);
        }
        Log.d("lolo", String.valueOf(titles.size()));
        completedListAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ImageButton playButton = (ImageButton) view.findViewById(R.id.playButtonOfflineVideoFeed);
//        playButton.setVisibility(View.GONE);
//    }
}
