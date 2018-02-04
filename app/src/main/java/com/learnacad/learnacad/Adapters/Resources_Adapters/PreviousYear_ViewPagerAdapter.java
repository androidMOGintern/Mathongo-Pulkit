package com.learnacad.learnacad.Adapters.Resources_Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.learnacad.learnacad.Fragments.Resources_Fragments.JeeAdvance_PreviousYearFragment;
import com.learnacad.learnacad.Fragments.Resources_Fragments.JeeMain_PreviousYearFragment;
import com.learnacad.learnacad.Fragments.Resources_Fragments.OtherExams_PreviousYearFragment;

/**
 * Created by sahil on 25/12/17.
 */

public class PreviousYear_ViewPagerAdapter extends FragmentStatePagerAdapter {

    public PreviousYear_ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

            return new JeeMain_PreviousYearFragment();
        }else if(position == 1){

            return new JeeAdvance_PreviousYearFragment();
        }else{

            return new OtherExams_PreviousYearFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
