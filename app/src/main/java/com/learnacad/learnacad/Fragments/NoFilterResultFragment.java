package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.learnacad.learnacad.R;

/**
 * Created by Sahil Malhotra on 25-08-2017.
 */

public class NoFilterResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_filter_result_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button clearFilters = view.findViewById(R.id.NoFilterResultFragment_ClearFiltersButton);

        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new LibraryCourseListFragment());
                fragmentTransaction.commit();
            }
        });
    }
}
