package com.learnacad.learnacad.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Models.SharedPrefManager;
import com.learnacad.learnacad.R;
import com.learnacad.learnacad.Service.MyReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.READ_SMS;

/**
 * Created by sahil on 31/12/17.
 */

public class Forgot_Password_OTP extends Fragment {


    View view;
    Unbinder unbinder;

    @BindView(R.id.forgot_password_otp_editText1)
    EditText editText1;

    @BindView(R.id.forgot_password_otp_editText2)
    EditText editText2;

    @BindView(R.id.forgot_password_otp_editText3)
    EditText editText3;

    @BindView(R.id.forgot_password_otp_editText4)
    EditText editText4;

    @BindView(R.id.forgot_password_otp_chronometerTextView)
    TextView timerTextView;

    @BindView(R.id.forgot_password_otp_submitButton)
    Button submitButton;

    @BindView(R.id.otpResendButton)
    Button otpResendButton;

    @BindView(R.id.pb)
    ProgressBar progressBar;


    int storedOTP;

    SharedPrefManager prefManager;
    String mobileNum,otp1,otp2,otp3,otp4;
    boolean isAllowedToRead;
    MyReceiver receiver;
    IntentFilter intentFilter;
    CountDownTimer timer;
    Context activityContext,applicationContext;
    int otp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgot_password_otp_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        activityContext = getActivity();
        applicationContext = activityContext.getApplicationContext();


        if(!checkPermission()){

            requestPermission();
        }
        intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        prefManager = new SharedPrefManager(activityContext);

        progressBar.setProgress(0);
        progressBar.setIndeterminate(true);

        verifyOTP();


        timer = new CountDownTimer(90000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timerTextView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {

                timerTextView.setText("");
                activateResendButton();


            }
        }.start();

        mobileNum = prefManager.getMobile();

        storedOTP = prefManager.getOTP();

        otpResendButton.setEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText2.requestFocus();
            }
        });


        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText3.requestFocus();
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText4.requestFocus();
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerTextView.setText(" ");
                timer.cancel();


                if(validateEditText()){

                    StringBuilder sb = new StringBuilder(otp1+otp2+otp3+otp4);

                    try {
                        int verificationCode = Integer.parseInt(sb.toString());

                        if (verificationCode == storedOTP) {

                            replaceFragment();
                        } else {

                            otpError();
                        }
                    }catch (NumberFormatException e){

                        otpError();
                    }

                }
            }
        });

        otpResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendOTP();
            }
        });


    }

    private boolean validateEditText() {

        otp1 = editText1.getText().toString().trim();
        otp2 = editText2.getText().toString().trim();
        otp3 = editText3.getText().toString().trim();
        otp4 = editText4.getText().toString().trim();

        if(TextUtils.isEmpty(otp1) || TextUtils.isEmpty(otp2) || TextUtils.isEmpty(otp3) || TextUtils.isEmpty(otp4) ||
                (otp1.length() != 1) || (otp2.length() != 1) || (otp3.length() != 1) || (otp4.length() != 1)){

            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Please enter a valid OTP")
                    .setTitleText("Invalid OTP")
                    .show();
//            Snackbar snackbar1 = Snackbar.make(view, "Please enter a verified OTP.", Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0, 0, 0, 0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();

            return false;
        }

        return true;
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
                            if(!success.contentEquals("success")){

                                progressBar.setVisibility(View.GONE);

                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("It seems there is a problem in receiving OTP at your end.Please try again later.")
                                        .show();

                            }else{

                                progressBar.setVisibility(View.GONE);
                                prefManager.setOTPSent(otp);

//                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//
//                                fragmentTransaction.replace(R.id.content_login_frame,new SignUp_Step2a_Fragment());
//                                fragmentTransaction.addToBackStack(null).commit();
                            }



                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101FP_OTR)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Please enter a valid mobile number or Check your Internet Connection.(202FP_OTR)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }



    public void activateResendButton(){

        otpResendButton.setEnabled(true);
        otpResendButton.setTextColor(Color.parseColor("#1589ee"));
    }

    private void verifyOTP() {

        receiver = new MyReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {


                if (isAllowedToRead) {

                    SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    SmsMessage smsMessage = msgs[0];
                    String messageBody = smsMessage.getDisplayMessageBody();
                    String[] splitBody = messageBody.split(" ");


                    if (splitBody.length > 3) {

                        try {
                            int verificationCode = Integer.parseInt(splitBody[0]);

                            if (verificationCode == storedOTP) {

                                editText1.setText("*");
                                editText2.setText("*");
                                editText3.setText("*");
                                editText4.setText("*");

                                timer.cancel();

                                replaceFragment();

                            } else {
                                otpError();
                            }

                        } catch (NumberFormatException e) {

                            otpError();

                        }
                    }

                }
            }
        };

        activityContext.registerReceiver(receiver,intentFilter);
    }

    private void requestPermission() {

        requestPermissions(new String[]{READ_SMS},2501);
    }

    private boolean checkPermission() {

        int readSMS = ContextCompat.checkSelfPermission(applicationContext, READ_SMS);


        isAllowedToRead = (readSMS == PackageManager.PERMISSION_GRANTED);
        return isAllowedToRead;

    }

    private void replaceFragment(){

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.content_login_frame,new Password_UpdateFragment());
        fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
    }

    public  void otpError(){

        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                .setContentText("OTP not verified.Please enter it again.")
                .setTitleText("Invalid OTP")
                .show();

//        Snackbar snackbar1 = Snackbar.make(view, "OTP not verified.Please enter it again.", Snackbar.LENGTH_LONG);
//        View view1 = snackbar1.getView();
//        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//        view1.setPadding(0, 0, 0, 0);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//        textView.setCompoundDrawablePadding(8);
//        snackbar1.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activityContext.unregisterReceiver(receiver);
        timer.cancel();
        timerTextView.setText(" ");
        unbinder.unbind();
    }
}
