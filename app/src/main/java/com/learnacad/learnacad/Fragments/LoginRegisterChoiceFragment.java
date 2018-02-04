package com.learnacad.learnacad.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.BaseActivity;
import com.learnacad.learnacad.Activities.LoginActivity;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.orm.SugarRecord.listAll;

/**
 * Created by sahil on 20/12/17.
 */

public class LoginRegisterChoiceFragment extends Fragment {

    View view;
    Unbinder unbinder;

    @BindView(R.id.loginButton_LoginRegisterFragment)
    Button loginButton;

    @BindView(R.id.signUpButton_LoginRegisterFragment)
    Button signupButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Context context;

        view = inflater.inflate(R.layout.loginregister_choice_fragment_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        context = getActivity();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FlurryAgent.logEvent("LoginButton_Login_OR_SignUp_Choice_Clicked");

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("LoginOrRegister",1);
                context.startActivity(intent);

            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FlurryAgent.logEvent("SignUpButton_Login_OR_SignUp_Choice_Clicked");


                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("LoginOrRegister",2);
                context.startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
