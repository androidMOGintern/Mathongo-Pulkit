package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.Library_Top_level_Adapter;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 18-06-2017.
 */

public class Library_Subjects_Fragment extends Fragment {

    Library_Top_level_Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> titlesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_subjects_list_fragmetnt_layout,container,false);

        recyclerView = view.findViewById(R.id.library_subjects_list_RecyclerView);
        titlesList = new ArrayList<>();
        adapter = new Library_Top_level_Adapter(getActivity(),titlesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        fetchTitles();
        return view;
    }

    private void fetchTitles() {

        for(int i = 0; i < 5; ++i){

            titlesList.add("Title" + " " + i);
        }

        adapter.notifyDataSetChanged();
    }
}
