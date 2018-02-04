package com.learnacad.learnacad.Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Models.SharedPrefManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sahil on 21/12/17.
 */

public class SignUp_Step1_Fragment extends Fragment {

    View view;
    Unbinder unbinder;

    @BindView(R.id.signUp_step1_MobileNumEditText)
    TextInputEditText mobileNumInputEditText;

    @BindView(R.id.signUp_step1_continueButton)
    Button continueButton;

    ProgressBar progressBar;

    @BindView(R.id.RegisterLoginButton)
    Button loginButton;

    SharedPrefManager sharedPrefManager;
    String mobileNum;
    Context context;
    boolean mobileUniqueAnswer;

    @BindView(R.id.signUp_step1_FragmentlogoImageView)
    ImageView headerImageView;


    int otp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_step1_layout,container,false);


        unbinder = ButterKnife.bind(this,view);
        context = getActivity();

        sharedPrefManager = new SharedPrefManager(getActivity());

        progressBar = view.findViewById(R.id.pb);

        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);


        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        Picasso.with(context)
                .load(R.drawable.logotwo)
                .into(headerImageView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_login_frame, new Login_Fragment());
                fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
            }
        });



        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.setVisibility(View.VISIBLE);
                if(validateMobileNumber()){

                    sharedPrefManager.setMobileNum(mobileNum);


                    AndroidNetworking.post(Api_Urls.BASE_URL + "signup/contact")
                            .addUrlEncodeFormBodyParameter("contact",mobileNum)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String success = response.getString("success");
                                        if(success.contentEquals("Unique")){

                                            sendOTP();



                                        }else{

                                            progressBar.setVisibility(View.GONE);
                                            sharedPrefManager.setMobileNum(null);

                                            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Already Registered")
                                                    .setContentText("Mobile Number already registered.\nPlease Login")
                                                    .setConfirmText("OK");

                                                   dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(R.id.content_login_frame, new Login_Fragment());
                                                            fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
                                                            dialog.cancel();
                                                        }
                                                    });

                                            dialog.setCancelable(true);
                                            dialog.show();

//                                            Snackbar snackbar1 = Snackbar.make(view,"Mobile Number already registered.Please Login",Snackbar.LENGTH_LONG);
//                                            View view1 = snackbar1.getView();
//                                            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                                            view1.setPadding(0,0,0,0);
//                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                                            textView.setCompoundDrawablePadding(8);
//
//                                            snackbar1.setAction("LOGIN", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//
//                                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                                    fragmentTransaction.replace(R.id.content_login_frame, new Login_Fragment());
//                                                    fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
//                                                }
//                                            });
//
//                                            snackbar1.show();
                                        }

                                    } catch (JSONException e) {

                                        progressBar.setVisibility(View.GONE);
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("There seems a problem with us.\nPlease try again later.(101SPS1_CO)")
                                                .setTitleText("Oops..!!")
                                                .show();


                                    }
                                }

                                @Override
                                public void onError(ANError anError) {


                                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Connection Error!\nPlease try again later.(202SPS1_CO)")
                                            .setTitleText("Oops..!!")
                                            .show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            });


                }else{

                    progressBar.setVisibility(View.GONE);

                    mobileNumInputEditText.setError("Enter a valid mobile number.");
//                    Snackbar snackbar1 = Snackbar.make(view,"Enter a valid mobile number.",Snackbar.LENGTH_LONG);
//                    View view1 = snackbar1.getView();
//                    TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                    view1.setPadding(0,0,0,0);
//                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                    textView.setCompoundDrawablePadding(8);
//                    snackbar1.show();
                }

            }
        });
    }

    public int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public void sendOTP() {

        otp = randomWithRange(1000,9999);

        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get("https://2factor.in/API/V1/7045a9f4-ed50-11e7-a328-0200cd936042/SMS/"+mobileNum+"/"+otp+"/MathongoOTP")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TOOINNGG","respnse");

                        try {
                            String success = response.getString("Status");
                            if(!success.contentEquals("Success")){

                                sendOTP();
                                sharedPrefManager.setOTPSent(0000);

                            }else{

                                progressBar.setVisibility(View.GONE);
                                sharedPrefManager.setOTPSent(otp);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                                fragmentTransaction.replace(R.id.content_login_frame,new SignUp_Step2a_Fragment());
                                fragmentTransaction.addToBackStack(null).commit();
                            }



                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101SPS1_OTS)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {


                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Please enter a valid mobile or Check your Internet Connection.(202SPS1_OTS)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);


                    }
                });
    }


    private boolean validateMobileNumber() {

        mobileNum = mobileNumInputEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(mobileNum) && mobileNum.length() == 10){

            return true;
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
