package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.learnacad.learnacad.R;

/**
 * Created by Sahil Malhotra on 07-06-2017.
 */

public class ContinueFeedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.continue_feed_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ProgressBar linearProgressBar = view.findViewById(R.id.linearProgressBarContinue);
        linearProgressBar.setMax(100);
        linearProgressBar.setProgress(50);

        CircleProgressView circleProgressView = view.findViewById(R.id.circularProgressBarContinue);
        circleProgressView.setTextEnabled(true);
        circleProgressView.setStartAngle(90);
        circleProgressView.setProgress(50);

        CardView cardView = view.findViewById(R.id.continueFeedFragmentCard);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "LOL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
