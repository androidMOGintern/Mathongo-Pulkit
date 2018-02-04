package com.learnacad.learnacad.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Fragments.Login_Fragment;
import com.learnacad.learnacad.Fragments.SignUp_Step1_Fragment;
import com.learnacad.learnacad.R;

import static com.orm.SugarRecord.listAll;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        frameLayout = (FrameLayout) findViewById(R.id.content_login_frame);

        Intent intent = getIntent();
        int whereToGo = intent.getIntExtra("LoginOrRegister",1);




                if(whereToGo == 1) {

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_login_frame, new Login_Fragment());
                    fragmentTransaction.commit();

                }else if(whereToGo == 2){

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_login_frame, new SignUp_Step1_Fragment());
                    fragmentTransaction.commit();

                }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("SPLASH_DONE",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
