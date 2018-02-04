package com.learnacad.learnacad.Models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sahil Malhotra on 07-10-2017.
 */

public class SharedPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    // MODE OF SHARED PREF
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Mathongo";
    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTime";
    private static final String OTP = "otp";
    private static final String MOBILE = "mobile number";

    public SharedPrefManager(Context context){
        this.mContext = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setOTPSent(int otp){

        editor.putInt(OTP,otp);
        editor.commit();
    }

    public int getOTP(){

        return pref.getInt(OTP,-9999);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setMobileNum(String mobileNum){

        editor.putString(MOBILE,mobileNum);
        editor.commit();
    }

    public String getMobile(){

        return pref.getString(MOBILE,null);
    }
}
