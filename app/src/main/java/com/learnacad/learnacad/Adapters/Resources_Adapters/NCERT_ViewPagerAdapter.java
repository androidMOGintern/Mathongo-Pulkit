package com.learnacad.learnacad.Adapters.Resources_Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.learnacad.learnacad.Fragments.Resources_Fragments.Class11_NCERTFragment;
import com.learnacad.learnacad.Fragments.Resources_Fragments.Class12_NCERTFragment;

/**
 * Created by sahil on 25/12/17.
 */

public class NCERT_ViewPagerAdapter extends FragmentStatePagerAdapter{


    public NCERT_ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            return new Class11_NCERTFragment();
        }else{

            return new Class12_NCERTFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
