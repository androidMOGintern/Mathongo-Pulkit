package com.learnacad.learnacad.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sahil on 31/12/17.
 */

public class Password_UpdateFragment extends Fragment {

    View view;
    Unbinder unbinder;

    @BindView(R.id.editTextCreatePasswordRegisterFragment)
    TextInputEditText editText_PasswordCreation;

    @BindView(R.id.editTextConfirmPasswordRegisterFragment)
    TextInputEditText editText_PasswordConfirm;

    @BindView(R.id.password_save_Button_Fragment)
    Button saveButton;

    @BindView(R.id.pb)
    ProgressBar progressBar;

    String createPassword,confirmPassword,mobileNum;
    SharedPrefManager prefManager;
    Student student;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.forgot_password_updation_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        prefManager = new SharedPrefManager(getActivity());

        mobileNum = prefManager.getMobile();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);


                if(validateEditTextFields()){

                    AndroidNetworking.post(Api_Urls.BASE_URL + "authorize/changepassword")
                            .addUrlEncodeFormBodyParameter("contact",mobileNum)
                            .addUrlEncodeFormBodyParameter("password",confirmPassword)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String success = response.getString("success");
                                        if(success.contentEquals("true")){

                                            authorizeUser();

                                        }else{

                                            showError("(101PAS_CP_EL)");
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    } catch (JSONException e) {
                                        showError("(101PAS_CP)");
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                    showError("(202PAS_CP)");
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                }else{

                    progressBar.setVisibility(View.GONE);

                }
            }
        });

    }

    private void authorizeUser() {


        AndroidNetworking.post(Api_Urls.BASE_URL+ "authorize")
                .addUrlEncodeFormBodyParameter("contact",mobileNum)
                .addUrlEncodeFormBodyParameter("password",confirmPassword)
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

                                progressBar.setVisibility(View.GONE);
                                gotoBaseActivity();
                            }
                            else{

                                String message = response.getString("message");


                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setContentText(message)
                                        .setTitleText("Wrong Credentials")
                                        .setConfirmText("Ok")
                                        .show();
                                progressBar.setVisibility(View.GONE);



                            }

                        } catch (JSONException e) {

                            Log.d("jsonrror",e.getLocalizedMessage());
                            Log.d("jsonrror",e.getMessage());
                            Log.d("jsonrror",e.toString());
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101PAS_AU)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);

                            //  snackbar.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("libraryerror",anError.getLocalizedMessage());
                        Log.d("libraryerror",anError.getErrorCode() + " ");


                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202PAS_AU)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);


                    }
                });

    }

    public void showError(String errorCode){

        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                .setContentText("There occured some error.\nPlease try again later."+ errorCode)
                .setTitleText("Error")
                .setConfirmText("Ok");

        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.cancel();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private boolean validateEditTextFields() {

        createPassword = editText_PasswordCreation.getText().toString().trim();
        confirmPassword = editText_PasswordConfirm.getText().toString().trim();

        if(TextUtils.isEmpty(createPassword)){

            editText_PasswordCreation.setError("Please enter a password");
            return false;
        }

        if(TextUtils.isEmpty(confirmPassword)){

            editText_PasswordCreation.setError("Please re enter password");
            return false;
        }

        if(createPassword != null && confirmPassword != null){

                if(!createPassword.contentEquals(confirmPassword)){

                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Invalid Password")
                            .setContentText("Password donot match")
                            .setContentText("Ok");

                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.cancel();
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();

                    return false;
                }


        }

        return true;

    }


    public void gotoBaseActivity(){

        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
