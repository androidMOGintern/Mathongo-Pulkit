package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.LPLectureTabAdapter;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 11-07-2017.
 */

public class LPLectureTabFragment extends Fragment {

    RecyclerView recyclerView;
    LPLectureTabAdapter adapter;
    ArrayList<Lecture> lectures;
    private static View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view != null){

            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){

                parent.removeAllViews();
            }
        }


        try {

            view = inflater.inflate(R.layout.lecture_player_lecturetab,container,false);
        }catch (InflateException e){


        }

        int selectedPosition  = getActivity().getIntent().getIntExtra("selectedPosition",0);

        recyclerView = view.findViewById(R.id.lecturePlayerLectureTabRecyclerView);
        lectures = new ArrayList<>();
        lectures = (ArrayList<Lecture>) getActivity().getIntent().getSerializableExtra("lectureList");
        adapter = new LPLectureTabAdapter(getActivity(),lectures,selectedPosition);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();

        return view;
    }


}
