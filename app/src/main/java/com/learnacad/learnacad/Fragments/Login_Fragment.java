package com.learnacad.learnacad.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.BaseActivity;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.learnacad.learnacad.Fragments.Register_Fragment.isValidEmail;
import static com.orm.SugarRecord.listAll;

/**
 * Created by Sahil Malhotra on 13-06-2017.
 */

public class Login_Fragment extends Fragment {

    public static View view;
    String mobileNum,password;
    Snackbar snackbar;
    String toSendBodyParam,myReferalCode,toNotSendParam;
    ProgressDialog pd;
    Student student;
    TextView textView_Forgotpass;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view != null){

            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){

                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_login,container,false);
        }catch (InflateException e){


        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final TextInputEditText mobileNumEditText = (TextInputEditText) view.findViewById(R.id.loginMobileNumEditText);
        final TextInputEditText passwordEditText = (TextInputEditText) view.findViewById(R.id.loginPasswordEditText);
        Button signUp = (Button) view.findViewById(R.id.loginSingupButton);
        Typeface typefaceMedium = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface typefaceRegular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
        mobileNumEditText.setTypeface(typefaceRegular);
        signUp.setTypeface(typefaceMedium);
        passwordEditText.setTypeface(typefaceRegular);

        textView_Forgotpass = view.findViewById(R.id.loginForgotPasswordTextView);

        textView_Forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FlurryAgent.logEvent("Forgot_Password_Clicked");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content_login_frame,new Forgot_Password_Mobile());
                fragmentTransaction.commit();
            }
        });

        mContext = getActivity();

        snackbar = Snackbar.make(view,null,Snackbar.LENGTH_INDEFINITE);


        pd = new ProgressDialog(getContext());

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content_login_frame,new SignUp_Step1_Fragment());
                fragmentTransaction.commit();
            }
        });

        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNum = mobileNumEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if (mobileNum.isEmpty()) {


                    mobileNumEditText.setError("Please enter your Mobile Number to continue.");
                    return;
                }


                if (password.isEmpty()) {


                     passwordEditText.setError("Please enter your Password to continue.");
//                    snackbar.dismiss();
//                    Snackbar snackbar1 = Snackbar.make(view,"Enter Password",Snackbar.LENGTH_LONG);
//                    View view1 = snackbar1.getView();
//                    TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                    view1.setPadding(0,0,0,0);
//                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                    textView.setCompoundDrawablePadding(8);
//                    snackbar1.show();
                    return;
                }




                    if (mobileNum.length() != 10 || (!TextUtils.isDigitsOnly(mobileNum))) {


                        mobileNumEditText.setError("Please enter a valid mobile number.");

//                        snackbar.dismiss();
//                        Snackbar snackbar1 = Snackbar.make(view, "Enter a valid mobile number", Snackbar.LENGTH_LONG);
//                        View view1 = snackbar1.getView();
//                        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                        view1.setPadding(0, 0, 0, 0);
//                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//                        textView.setCompoundDrawablePadding(8);
//                        snackbar1.show();
                        return;
                    }


                if(isConnected()){

                    authorizeUser(mobileNum,password,view);
                    FlurryAgent.logEvent("LoginButton_loginFragment_Clicked");


                }else{
                    snackbar.dismiss();

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("No Internet Conncetion.\n Please try again later.")
                            .setTitleText("No Connection")
                            .show();

                    return;

                }


            }
        });
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void authorizeUser(String mobileNum, String password, final View rootview) {


        snackbar.setText("Logging you in...");
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greencircle,0,0,0);
            view.setPadding(0,0,0,0);
        textView.setCompoundDrawablePadding(8);
            snackbar.show();
        AndroidNetworking.post(Api_Urls.BASE_URL+ "authorize")
                .addUrlEncodeFormBodyParameter("contact",mobileNum)
                .addUrlEncodeFormBodyParameter("password",password)
                .addUrlEncodeFormBodyParameter("email","")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("success");
                            if(result.contentEquals("true")){

                                String tokenId = response.getString("token");
                                JSONObject s = response.getJSONObject("student");
                                student = new Student();

                                student.setName(s.getString("name"));
                                student.setStudentId(Integer.parseInt(s.getString("id")));
                                student.setClassChoosen(s.getString("class"));
                                student.setMobileNum(s.getString("contact"));


                                SessionManager session = new SessionManager(tokenId);

                                SugarRecord.deleteAll(Student.class);
                                SugarRecord.save(session);
                                SugarRecord.save(student);
                                snackbar.dismiss();
                                gotoBaseActivity();
                            }
                            else{

                                String message = response.getString("message");

                                snackbar.dismiss();

                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setContentText(message)
                                        .setTitleText("Wrong Credentials")
                                        .setConfirmText("Ok")
                                        .show();
//
//                                Snackbar snackbar1 = Snackbar.make(rootview,message  + ".Please try again.",Snackbar.LENGTH_LONG);
//                                View view = snackbar1.getView();
//                                TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                                view.setPadding(0,0,0,0);
//                                textView.setCompoundDrawablePadding(8);
//                                snackbar1.show();


                            }

                        } catch (JSONException e) {

                            Log.d("jsonrror",e.getLocalizedMessage());
                            Log.d("jsonrror",e.getMessage());
                            Log.d("jsonrror",e.toString());
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LOG_AU)")
                                    .setTitleText("Oops..!!")
                                    .show();
                          //  snackbar.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("libraryerror",anError.getLocalizedMessage());
                        Log.d("libraryerror",anError.getErrorCode() + " ");


                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LOG_AU)")
                                .setTitleText("Oops..!!")
                                .show();

                    }
                });

    }



    public void gotoBaseActivity(){

        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

}

