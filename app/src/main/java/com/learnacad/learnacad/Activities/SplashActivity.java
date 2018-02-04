package com.learnacad.learnacad.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.google.firebase.messaging.FirebaseMessaging;
import com.learnacad.learnacad.BuildConfig;
import com.learnacad.learnacad.Fragments.LoginRegisterChoiceFragment;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.SharedPrefManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.Networking.ForceUpdateChecker;
import com.learnacad.learnacad.Notifications.Config;
import com.learnacad.learnacad.Notifications.NotificationHandler;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

import static com.orm.SugarRecord.listAll;

public class SplashActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {

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

    private BroadcastReceiver mRegisterationBroadcastReceiver;
    private SharedPrefManager sharedPrefManager;

    @BindView(R.id.content_splash_frame)
    public FrameLayout frameLayout;

    @BindView(R.id.splashRelativeLayout)
    public RelativeLayout splashRelativeLayout;

    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        Fabric.with(this, new Crashlytics());

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Intent intent = getIntent();


        boolean isSplashDone = intent.getBooleanExtra("SPLASH_DONE",false);

        if(!isSplashDone) {

            frameLayout.setVisibility(View.GONE);
            splashRelativeLayout.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    splashRelativeLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }

        AndroidNetworking.initialize(getApplicationContext());



        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

        mRegisterationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(Config.REGISTRATION_COMPLETE)){

                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                }
            }
        };



        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if(isConnected()) {

            checkForUpdate();

        }else{

            moveToNext();

        }

    }


    private void checkForUpdate() {

        AndroidNetworking.get(Api_Urls.BASE_URL + "authorize/version")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String success = response.getString("success");

                            if(success.contentEquals("true")){

                                JSONObject ver = response.getJSONObject("version");
                                version = ver.getString("version_code");
                                verifyUpdate();
                            }else{

                                SweetAlertDialog dialog = new SweetAlertDialog(SplashActivity.this,SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("There occured some error.\nPlease try again later.(101SP_VE)")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                                startMain.addCategory(Intent.CATEGORY_HOME);
                                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(startMain);
                                            }
                                        });


                                dialog.setCancelable(false);
                                dialog.show();
                            }
                        } catch (JSONException e) {


                            SweetAlertDialog dialog = new SweetAlertDialog(SplashActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There occured some error.\nPlease try again later.(101SP_VE)")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                                            startMain.addCategory(Intent.CATEGORY_HOME);
                                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(startMain);
                                        }
                                    });


                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {


                        SweetAlertDialog dialog = new SweetAlertDialog(SplashActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("There occured some error.\nPlease try again later.(202SP_VE)")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startMain);
                                    }
                                });


                        dialog.setCancelable(false);
                        dialog.show();

                    }
                });
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

    private void verifyUpdate() {


        if(!version.contentEquals(BuildConfig.VERSION_NAME)){

            SweetAlertDialog dialog = new SweetAlertDialog(SplashActivity.this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("A New Version is now available.\nUpdate to continue learning.")
                    .setConfirmText("Update")
                    .setTitleText("MathonGo is better now !")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            LogoutAsyncTask logoutTask = new LogoutAsyncTask();
                            logoutTask.execute(Api_Urls.BASE_URL);
                        }
                    });

            dialog.setCancelable(false);
            dialog.show();
        }else{

            moveToNext();
        }
    }

    private void redirectToStore() {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Api_Urls.PLAYSTORE_LINK));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void moveToNext(){

        List<SessionManager> sessionManager = listAll(SessionManager.class);
        if (sessionManager != null) {

            if (sessionManager.size() > 0) {


                startActivity(new Intent(this, BaseActivity.class));
                finish();
            }else{

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_splash_frame,new LoginRegisterChoiceFragment());
                    fragmentTransaction.commitAllowingStateLoss();


            }
        }else{


                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_splash_frame,new LoginRegisterChoiceFragment());
                fragmentTransaction.commitAllowingStateLoss();


        }
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegisterationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegisterationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegisterationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationHandler.clearNotifications(getApplicationContext());

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {


    }


    private void logout(boolean message) {

        Log.d("tutu","Logout functin called " + message);

        if(message) {
            SugarRecord.deleteAll(SessionManager.class);
            SugarRecord.deleteAll(Student.class);
            redirectToStore();
        }else{

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Connection Error!\nPlease try again later.(202SA_LO)")
                    .setTitleText("Oops..!!")
                    .show();

        }
    }

    public class LogoutAsyncTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();

            try {
                URL url = new URL(Api_Urls.BASE_URL + "logout");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                if(inputStream == null){
                    return null;
                }

                Scanner sc = new Scanner(inputStream);

                while(sc.hasNext()){

                    sb.append(sc.nextLine());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("tutu",s);

            try {
                JSONObject obj = new JSONObject(s);
                String success = obj.getString("success");
                Log.d("tutu","success = " + success);
                if(success.contentEquals("true")){

                    logout(true);
                }else{

                    logout(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
