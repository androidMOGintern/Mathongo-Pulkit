package com.learnacad.learnacad.Fragments;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Activities.BaseActivity;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.SharedPrefManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.SUGAR;
import static com.orm.SugarRecord.findAll;
import static com.orm.SugarRecord.listAll;

/**
 * Created by sahil on 22/12/17.
 */

public class SignUp_Step3_Fragment extends Fragment {

    Bundle bundle;



    View view;
    Unbinder unbinder;


    @BindView(R.id.signUp_step3_submitButton)
    Button submitButton;

    @BindView(R.id.sign_up_step3_targetYear2018_CardView)
    CardView targetY2018CardView;

    @BindView(R.id.sign_up_step3_targetYear2019_CardView)
    CardView targetY2019CardView;

    @BindView(R.id.sign_up_step3_targetYear2020_CardView)
    CardView targetY2020CardView;

    @BindView(R.id.sign_up_step3_mode_coaching_CardView)
    CardView coachingCardView;

    @BindView(R.id.sign_up_step3_mode_selfStudy_CardView)
    CardView selfStudyCardView;

    @BindView(R.id.targetY2018TextView)
    TextView textView2018;

    @BindView(R.id.targetY2019TextView)
    TextView textView2019;

    @BindView(R.id.targetY2020TextView)
    TextView textView2020;

    @BindView(R.id.selfStudyMode_TextView)
    TextView selfStudyTextView;

    @BindView(R.id.coaching_TextView)
    TextView coachingTextView;


    ProgressBar progressBar;

    Student student;


    int color,grayColor;
    ArrayList<String> targetYearChoices,modeOfStudyChoices;
    Context activityContext;
    String mobileNum;
    boolean is2019Selected,is2018Selected,is2020Selected,isSelfStudySelected,isCoachingSelected;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sign_up_step3_layout,container,false);

        bundle = this.getArguments();

        is2018Selected = is2019Selected = is2020Selected = isSelfStudySelected = isCoachingSelected = false;

        unbinder = ButterKnife.bind(this,view);
        activityContext = getActivity();

        progressBar = view.findViewById(R.id.pb);


        targetYearChoices = new ArrayList<>();
        modeOfStudyChoices = new ArrayList<>();

        color = Color.parseColor("#1589ee");
        grayColor = Color.parseColor("#6885a5");

        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);

        SugarRecord.deleteAll(Student.class);

        return view;
    }

    public void activateSS(){

        selfStudyCardView.setCardBackgroundColor(color);
        selfStudyTextView.setTextColor(color);
        modeOfStudyChoices.add("Self Study");
    }

    public void activateC(){

        coachingCardView.setCardBackgroundColor(color);
        coachingTextView.setTextColor(color);
        modeOfStudyChoices.add("Coaching");
    }

    public void deactivateSS(){

        selfStudyCardView.setCardBackgroundColor(Color.WHITE);
        selfStudyTextView.setTextColor(grayColor);
        modeOfStudyChoices.remove("Self Study");
    }

    public void deactivateC(){

        coachingCardView.setCardBackgroundColor(Color.WHITE);
        coachingTextView.setTextColor(grayColor);
        modeOfStudyChoices.remove("Coaching");
    }

    public void activate2018(){

        targetY2018CardView.setCardBackgroundColor(color);
        textView2018.setTextColor(color);
        targetYearChoices.add("2018");
    }

    public void activate2019(){

        targetY2019CardView.setCardBackgroundColor(color);
        textView2019.setTextColor(color);
        targetYearChoices.add("2019");
    }

    public void activate2020(){

        targetY2020CardView.setCardBackgroundColor(color);
        textView2020.setTextColor(color);
        targetYearChoices.add("2020");
    }


    public void deactivate2018(){


        targetY2018CardView.setCardBackgroundColor(Color.WHITE);
        textView2018.setTextColor(grayColor);
        targetYearChoices.remove("2018");
    }


    public void deactivate2019(){


        targetY2019CardView.setCardBackgroundColor(Color.WHITE);
        textView2019.setTextColor(grayColor);
        targetYearChoices.remove("2019");
    }



    public void deactivate2020(){


        targetY2020CardView.setCardBackgroundColor(Color.WHITE);
        textView2020.setTextColor(grayColor);
        targetYearChoices.remove("2020");
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {



        targetY2018CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!is2018Selected){

                    is2018Selected = true;
                    is2019Selected = false;
                    is2020Selected = false;
                    activate2018();
                    deactivate2019();
                    deactivate2020();
                }else{

                    is2018Selected = false;
                    deactivate2018();
                }
            }
        });

        targetY2019CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!is2019Selected){

                    is2019Selected = true;
                    activate2019();
                    deactivate2018();
                    deactivate2020();
                    is2018Selected = false;
                    is2020Selected = false;
                }else{

                    is2019Selected = false;
                    deactivate2019();
                }
            }
        });



        targetY2020CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!is2020Selected){

                    is2020Selected = true;
                    is2019Selected = false;
                    is2018Selected = false;
                    activate2020();
                    deactivate2019();
                    deactivate2018();
                }else{

                    is2020Selected = false;
                    deactivate2020();
                }
            }
        });


        selfStudyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isSelfStudySelected){

                    isSelfStudySelected = true;
                    isCoachingSelected = false;
                    deactivateC();
                    activateSS();
                }else{

                    isSelfStudySelected = false;
                    deactivateSS();
                }
            }
        });

        coachingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isCoachingSelected){

                    isSelfStudySelected = false;
                    deactivateSS();

                    isCoachingSelected = true;
                    activateC();
                }else{

                    isCoachingSelected = false;
                    deactivateC();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(targetYearChoices.size() == 1 && modeOfStudyChoices.size() == 1){

                        bundle.putString("class",targetYearChoices.get(0));
                        bundle.putString("mode_of_study",modeOfStudyChoices.get(0));

                        storeDataInDatabase();

                }else{

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Please select atleast one from each.")
                            .setTitleText("Nothing Selected")
                            .show();
//
//                    Snackbar snackbar1 = Snackbar.make(view,"Please select atleast one from each.",Snackbar.LENGTH_LONG);
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


    private void storeDataInDatabase() {

        progressBar.setVisibility(View.VISIBLE);

        String name = bundle.getString("name");
        String email = bundle.getString("email");
        final String password = bundle.getString("password");
        mobileNum = bundle.getString("mobile");

        AndroidNetworking.post(Api_Urls.BASE_URL + "signup/newstudent")
                .addUrlEncodeFormBodyParameter("name",name)
                .addUrlEncodeFormBodyParameter("email",email)
                .addUrlEncodeFormBodyParameter("password",password)
                .addUrlEncodeFormBodyParameter("contact",mobileNum)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String success = response.getString("success");

                            if(success.contentEquals("true")){

                                JSONObject studentObj = response.getJSONObject("student");

                                String name = studentObj.getString("name");
                                int studentId = studentObj.getInt("id");
                                String email = studentObj.getString("email");
                                String mobileNum = studentObj.getString("contact");


                                student = new Student(name,email,password,mobileNum,null,null);
                                student.setStudentId(studentId);
                                SugarRecord.save(student);
                                updateStudentDataInDatabase();

                            }else{

                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("There seems a problem with us.\nPlease try again later.(101SPS3_NS_EL)")
                                        .setTitleText("Oops..!!")
                                        .show();
                                progressBar.setVisibility(View.GONE);



                            }

                        } catch (JSONException e) {

                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101SPS3_NS)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202SPS3_NS)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void updateStudentDataInDatabase() {

        final String pincode = bundle.getString("pincode");
        final String classChosen = bundle.getString("class");
        String modeOfStudy = bundle.getString("mode_of_study");
        String targetExam = bundle.getString("prefered_exam");

        AndroidNetworking.put(Api_Urls.BASE_URL + "signup/newstudent/"+mobileNum)
                .addUrlEncodeFormBodyParameter("class",classChosen)
                .addUrlEncodeFormBodyParameter("prefered_exam",targetExam)
                .addUrlEncodeFormBodyParameter("pincode",pincode)
                .addUrlEncodeFormBodyParameter("mode_of_study",modeOfStudy)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            String success = response.getString("success");
                            if(success.contentEquals("true")){


                                authorizeUser();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101SPS3_UP)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202SPS3_UP)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);


                    }
                });
    }

//    private void authorizeUser() {
//
//
//        AndroidNetworking.post(Api_Urls.BASE_URL+ "authorize")
//                .addUrlEncodeFormBodyParameter("contact",mobileNum)
//                .addUrlEncodeFormBodyParameter("password",password)
//                .addUrlEncodeFormBodyParameter("email","")
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String result = response.getString("success");
//                            if(result.contentEquals("true")){
//
//                                String tokenId = response.getString("token");
//
//                                Student student = new Student();
//
//                                student.setName(response.getString("name"));
//                                SugarRecord.save(student);
//
//                                SessionManager session = new SessionManager(tokenId);
//                                SugarRecord.save(session);
//
//                                Log.d("totot",usedReferalCode);
//
//                                if(!usedReferalCode.contentEquals("NONE")) {
//
//                                    Log.d("totot","Inside ifff");
//                                    Log.d("totot",usedReferalCode);
//
//                                    pushReferalMapping(tokenId,usedReferalCode);
//
//                                }else{
//
//                                    gotoNextActivity();
//                                }
//
//                            }
//                            else{
//
//                                String message = response.getString("message");
//                                Log.d("totot","in else part");
//
//
//                                Snackbar snackbar1 = Snackbar.make(view,message  + ".Please try again.",Snackbar.LENGTH_LONG);
//                                View view = snackbar1.getView();
//                                TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                                view.setPadding(0,0,0,0);
//                                textView.setCompoundDrawablePadding(8);
//                                snackbar1.show();
//
//
//                            }
//
//                        } catch (JSONException e) {
//                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
//                                    .setContentText("There seems a problem with us.\nPlease try again later.")
//                                    .setTitleText("Oops..!!")
//                                    .show();
//                            //  snackbar.dismiss();
//
//                            Log.d("totot","catch part");
//                        }
//
//                        gotoNextActivity();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                        Log.d("libraryerror",anError.getLocalizedMessage());
//                        Log.d("libraryerror",anError.getErrorCode() + " ");
//
//
//                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
//                                .setContentText("Connection Error!\nPlease try again later.")
//                                .setTitleText("Oops..!!")
//                                .show();
//
//                    }
//                });
//
//    }

    private void gotoNextActivity() {

        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SPLASH SHOWN",true);
        startActivity(intent);
    }

    public void authorizeUser(){


        String password = bundle.getString("password");
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

                                SessionManager session = new SessionManager(tokenId);

                                SugarRecord.save(session);

                                progressBar.setVisibility(View.GONE);
                                gotoNextActivity();
                            }
                            else{


                                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Sign Up Error")
                                        .setContentText("Please contact administrator at info@mathongo.com")
                                        .setConfirmText("OK");

                                dialog.setCancelable(true);
                                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        dialog.cancel();
                                    }
                                });

                                dialog.show();

                                progressBar.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {

                            progressBar.setVisibility(View.GONE);
                            Log.d("jsonrror",e.getLocalizedMessage());
                            Log.d("jsonrror",e.getMessage());
                            Log.d("jsonrror",e.toString());
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101SPS3_AU)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            //  snackbar.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("libraryerror",anError.getLocalizedMessage());
                        Log.d("libraryerror",anError.getErrorCode() + " ");

                        progressBar.setVisibility(View.GONE);
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202SPS3_AU)")
                                .setTitleText("Oops..!!")
                                .show();

                    }
                });
    }


    // function to store the mapping between the students who used the referal code
    // and the student whose referal code is used
    // in this the referal code of the student who used the code is id sent
    // and the referal code he/she used

//    private void pushReferalMapping(String tokenId,String usedReferalCode) {
//        pd.setMessage("Fetching data...");
//        pd.show();
//
//        Log.d("totot","Inside pushReferalMapping");
//        AndroidNetworking.post(Api_Urls.BASE_URL + "authorize/addcode/" + student.getStudentId())
//                .addUrlEncodeFormBodyParameter("refercode",usedReferalCode)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String success = response.getString("success");
//                            if(!success.contentEquals("true ")){
//
//                                // TODO show the apt error
//                            }
//
//                            pd.hide();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                            // TODO show the apt error
//
//                            pd.hide();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                        Log.d("opopop",anError.toString());
//                        Log.d("opopop",anError.getErrorBody());
//                        Log.d("opopop", String.valueOf(anError.getErrorCode()));
//                        Log.d("opopop",anError.getErrorDetail());
//
//                        pd.hide();
//
//                    }
//                });
//    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
