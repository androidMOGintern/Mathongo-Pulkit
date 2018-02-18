package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.learnacad.learnacad.R;

/**
 * Created by Sahil Malhotra on 02-10-2017.
 */

public class EmptyReviewsStateFragment extends Fragment {

    Button rateAndreviewButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_empty_state_layout,container,false);

        rateAndreviewButton = view.findViewById(R.id.empty_reviews_state_rateandreview_button);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
}
