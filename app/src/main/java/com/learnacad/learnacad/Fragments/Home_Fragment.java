package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnacad.learnacad.Adapters.ViewPagerAdapter;
import com.learnacad.learnacad.R;

/**
 * Created by Sahil Malhotra on 08-06-2017.
 */

public class Home_Fragment extends Fragment{


    private ViewPager viewPager;
    private static View v;
    private TabLayout tabLayout;
    private String[] pageTitles = {"Feed", "My Courses","Bookmark","Offline Videos"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = v.findViewById(R.id.viewPager);


        tabLayout = v.findViewById(R.id.tablayout);

        for(int i = 0 ; i < 4; ++i){

            tabLayout.addTab(tabLayout.newTab().setText(pageTitles[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        viewPager.setAdapter(null);
    }
}
