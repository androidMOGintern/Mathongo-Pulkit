package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.RecommendedListAdapter;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 07-06-2017.
 */

public class RecommendedFragment extends Fragment{

        RecyclerView recyclerView;
        RecommendedListAdapter recommendedListAdapter;
        ArrayList<String> titleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommended_fragment_layout,container,false);

        recyclerView = v.findViewById(R.id.recommendedRecyclerViewList);
        titleList = new ArrayList<>();
        recommendedListAdapter = new RecommendedListAdapter(getActivity(),titleList);
        recyclerView.setAdapter(recommendedListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchResultsTemp();
        return v;
    }

    private void fetchResultsTemp() {


        for(int i = 0; i < 5; ++i){

            titleList.add("Title" + " " + i);
        }
       // Log.d("lolo",titleList.size() + " ");
        recommendedListAdapter.notifyDataSetChanged();
    }

}
