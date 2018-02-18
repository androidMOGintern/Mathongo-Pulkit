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

import com.learnacad.learnacad.Adapters.OfflineVideoListAdapter;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 03-06-2017.
 */

public class OfflineVideos_Fragment extends Fragment {

    RecyclerView recyclerView;
    OfflineVideoListAdapter offlineVideoListAdapter;
    ArrayList<String> titles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.offline_feed_fragment_layout,container,false);

        recyclerView = v.findViewById(R.id.offlineVideosRecyclerView);
        titles = new ArrayList<>();
        offlineVideoListAdapter = new OfflineVideoListAdapter(getActivity(),titles);
        recyclerView.setAdapter(offlineVideoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchTitles();
        return v;
    }

    private void fetchTitles() {

        for(int i = 0; i < 5; ++i){

            titles.add("Title" + " " + i);
        }
        Log.d("lolo", String.valueOf(titles.size()));
        offlineVideoListAdapter.notifyDataSetChanged();
    }


}
