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

import com.learnacad.learnacad.Adapters.Resources_Adapters.Formula_ViewPagerAdapter;
import com.learnacad.learnacad.Adapters.Resources_Adapters.QuestionBank_ViewPagerAdapter;
import com.learnacad.learnacad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sahil on 25/12/17.
 */

public class QuestionBankFragment extends Fragment {

    Unbinder unbinder;
    View view;

    @BindView(R.id.questionBankFragment_TabLayout)
    TabLayout tabLayout;

    @BindView(R.id.questionBank_Fragment_ViewPager)
    ViewPager viewPager;

    ActionBar actionBar;

    private static String[] tabTitles = {"Class XI","Class XII"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.question_bank_fragment_layout,container,false);

        unbinder =  ButterKnife.bind(this,view);

        actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();

        getActivity().setTitle("Question Bank");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        for(int i = 0; i < 2; i++){

            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        QuestionBank_ViewPagerAdapter adapter = new QuestionBank_ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);


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
