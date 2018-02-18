package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.EnrolledListAdapter;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 10-06-2017.
 */

public class Enrolled_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> titlesList;
    EnrolledListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.enrolled_mycourses_fragment_layout,container,false);

        recyclerView = v.findViewById(R.id.enrolledmyCoursesList);
        titlesList = new ArrayList<>();
        adapter = new EnrolledListAdapter(getActivity(),titlesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        fetchTitles();
        return v;
    }

    public void fetchTitles() {

        for(int i = 0; i < 5; ++i){

            titlesList.add("Title" + " " + i);
        }

        adapter.notifyDataSetChanged();
    }


}
