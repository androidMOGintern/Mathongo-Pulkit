package com.learnacad.learnacad.Fragments;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Models.SharedPrefManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.learnacad.learnacad.Service.MyReceiver;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.READ_SMS;

/**
 * Created by sahil on 21/12/17.
 */

public class SignUp_Step2_Fragment extends Fragment {

    Context applicationContext,activityContext;

    IntentFilter intentFilter;
    SharedPrefManager prefManager;
    Unbinder unbinder;
    View view;
    String mobileNum,name,email,password,pincode;
    int storedOTP;

    @BindView(R.id.editTextNameRegisterFragment)
    TextInputEditText nameEditText;

    @BindView(R.id.editTextCreatePasswordRegisterFragment)
    TextInputEditText passwordEditText;

    @BindView(R.id.editTextEmailRegisterFragment)
    TextInputEditText emailEditText;

    @BindView(R.id.buttonRegisterRegisterFragment)
    Button registerButton;

    @BindView(R.id.editTextPinCodeRegisterFragment)
    TextInputEditText pinCodeEditText;

    ProgressBar progressBar;

    Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        applicationContext = getActivity().getApplicationContext();
        activityContext = getActivity();
        prefManager = new SharedPrefManager(activityContext);
        intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        storedOTP = prefManager.getOTP();

        view = inflater.inflate(R.layout.sign_up_step2_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        progressBar = view.findViewById(R.id.pb);

        mobileNum = prefManager.getMobile();

        bundle = new Bundle();

        bundle.putString("mobile",mobileNum);

        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);

        return view;

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if(validateEditTextFields()){


                        AndroidNetworking.get("http://postalpincode.in/api/pincode/"+pincode)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            String successMsg = response.getString("Status");
                                            if(successMsg.contentEquals("Success")){


                                                progressBar.setVisibility(View.GONE);
                                                replaceFragment();

                                            }else{

                                                pinCodeEditText.setError("Not a valid Pincode.");
                                                progressBar.setVisibility(View.GONE);
                                                return;

                                            }
                                        } catch (JSONException e) {

                                            progressBar.setVisibility(View.GONE);
                                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                                    .setContentText("There seems a problem with us.\nPlease try again later.(101SPS2_PCV)")
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
                    return;
                }
            }
        });
    }

//    private void storeDataInDatabase() {
//
//        AndroidNetworking.post(Api_Urls.BASE_URL + "signup/newstudent")
//                .addUrlEncodeFormBodyParameter("name",name)
//                .addUrlEncodeFormBodyParameter("email",email)
//                .addUrlEncodeFormBodyParameter("password",password)
//                .addUrlEncodeFormBodyParameter("contact",mobileNum)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject student = response.getJSONObject("student");
//
//                            String name = student.getString("name");
//                            int studentId = student.getInt("id");
//                            String email = student.getString("email");
//                            String mobileNum = student.getString("contact");
//
//
//                            Student student1 = new Student(name,email,password,mobileNum,null,null);
//                            student1.setStudentId(studentId);
//
//
//                            if(isReferalPresent){
//
//
//                                student1.setUsedReferalCode(referalCode);
//
//                            }
//
//                            replaceFragment();
//
//                            SugarRecord.save(student1);
//                            pd.hide();
//
//                        } catch (JSONException e) {
//                            pd.hide();
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                        pd.hide();
//
//                    }
//                });
//    }

    private void replaceFragment() {

        bundle.putString("name",name);
        bundle.putString("email",email);
        bundle.putString("password",password);
        bundle.putString("pincode",pincode);


        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        SignUp_Step2b_Fragment fragment = new SignUp_Step2b_Fragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_login_frame, fragment);
        fragmentTransaction.commit();

    }

    private boolean validateEditTextFields() {

        name = nameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        pincode = pinCodeEditText.getText().toString().trim();

        if(TextUtils.isEmpty(name)){

              nameEditText.setError("Please enter your name");
//            Snackbar snackbar1 = Snackbar.make(view,"Enter your name",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;

        }

        if(name.length() < 3 ){

            nameEditText.setError("Pl");
//            Snackbar snackbar1 = Snackbar.make(view,"Please enter a valid email.",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;

        }

        if (TextUtils.isEmpty(email)){


              emailEditText.setError("Please enter your email id.");
//            Snackbar snackbar1 = Snackbar.make(view,"Enter email",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;
        }

        if(!isValidEmail(email)){

                emailEditText.setError("Please enter a valid email address.");
//
//            Snackbar snackbar1 = Snackbar.make(view,"Enter a valid email",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;
        }

        if (TextUtils.isEmpty(password)){

              passwordEditText.setError("Enter a password");
//            Snackbar snackbar1 = Snackbar.make(view,"Enter password",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;
        }


        if(TextUtils.isEmpty(pincode) || (pincode.length() != 6)){

            pinCodeEditText.setError("Please enter a valid pincode");

//            Snackbar snackbar1 = Snackbar.make(view,"Enter a valid pincode.",Snackbar.LENGTH_LONG);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0,0,0,0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
            return false;
        }

        return true;

//            String otpStr = otpEditText.getText().toString().trim();
//
//            if(!TextUtils.isEmpty(otpStr)) {
//
//                int otp = Integer.parseInt(otpStr);
//
//                if (!(otp == storedOTP)) {
//
//                    Snackbar snackbar1 = Snackbar.make(view, "OTP not verified.Please enter it again.", Snackbar.LENGTH_LONG);
//                    View view1 = snackbar1.getView();
//                    TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                    view1.setPadding(0, 0, 0, 0);
//                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//                    textView.setCompoundDrawablePadding(8);
//                    snackbar1.show();
//                    return false;
//                }
//            }else{
//
//
//                Snackbar snackbar1 = Snackbar.make(view, "Please enter the otp to continue.", Snackbar.LENGTH_LONG);
//                View view1 = snackbar1.getView();
//                TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                view1.setPadding(0, 0, 0, 0);
//                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//                textView.setCompoundDrawablePadding(8);
//                snackbar1.show();
//                return false;
//            }




    }



//    public boolean isReferal(){
//
//        referalCode = referalCodeEditText.getText().toString().trim();
//
//        if(!TextUtils.isEmpty(referalCode)) {
//
//
//            if ( referalCode.length() > 0 && referalCode.length() == 7) {
//
//
//                return true;
//            }
//
//
//            Snackbar snackbar1 = Snackbar.make(view, "Please enter a valid referal code.", Snackbar.LENGTH_SHORT);
//            View view1 = snackbar1.getView();
//            TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//            view1.setPadding(0, 0, 0, 0);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//            textView.setCompoundDrawablePadding(8);
//            snackbar1.show();
//            return false;
//        }else{
//
//            return false;
//        }
//    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
//
//timer.cancel();
//
//        if(validateEditTextFields()){
//
//
//        if(isReferalPresent) {
//
//        pd.show();
//        AndroidNetworking.post(Api_Urls.BASE_URL + "authorize/check/" + referalCode)
//        .build()
//        .getAsJSONObject(new JSONObjectRequestListener() {
//@Override
//public void onResponse(JSONObject response) {
//        try {
//        String success = response.getString("success");
//        if(success.contentEquals("Exists")){
//
//        storeDataInDatabase();
//
//        }else{
//
//        pd.hide();
//        Snackbar snackbar1 = Snackbar.make(view, "Please enter a valid referal code.", Snackbar.LENGTH_LONG);
//        View view1 = snackbar1.getView();
//        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//        view1.setPadding(0, 0, 0, 0);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//        textView.setCompoundDrawablePadding(8);
//        snackbar1.show();
//        return;
//        }
//
//        } catch (JSONException e) {
//        pd.hide();
//        e.printStackTrace();
//        }
//        }
//
//@Override
//public void onError(ANError anError) {
//
//
//        Log.d("opopopop",anError.getErrorDetail());
//        Log.d("opopopop", String.valueOf(anError.getResponse()));
//        Log.d("opopopop",anError.getLocalizedMessage());
//        Log.d("opopopop", String.valueOf(anError.getErrorCode()));
//
//        pd.hide();
//        Snackbar snackbar1 = Snackbar.make(view, "Connection Error!.Please try again later.", Snackbar.LENGTH_LONG);
//        View view1 = snackbar1.getView();
//        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//        view1.setPadding(0, 0, 0, 0);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);
//        textView.setCompoundDrawablePadding(8);
//        snackbar1.show();
//        return;
//
//        }
//        });
//        }else{
//
//
//        storeDataInDatabase();
//
//        }
//        }