package com.learnacad.learnacad.Fragments.Resources_Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.learnacad.learnacad.Adapters.Resources_Adapters.Formula_ViewPagerAdapter;
import com.learnacad.learnacad.Adapters.Resources_Adapters.PreviousYear_ViewPagerAdapter;
import com.learnacad.learnacad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sahil on 25/12/17.
 */

public class PreviousYearFragment extends Fragment {

    Unbinder unbinder;
    View view;

    @BindView(R.id.previousYearFragment_TabLayout)
    TabLayout tabLayout;

    @BindView(R.id.previousYear_Fragment_ViewPager)
    ViewPager viewPager;

    ActionBar actionBar;



    private static String[] tabTitles = {"JEE Mains","JEE Advance","Other Exams"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.previous_year_fragment_layout,container,false);

        unbinder =  ButterKnife.bind(this,view);

        actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();

        getActivity().setTitle("Previous Year Questions");


        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        for(int i = 0; i < 3; i++){

            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        PreviousYear_ViewPagerAdapter adapter = new PreviousYear_ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
